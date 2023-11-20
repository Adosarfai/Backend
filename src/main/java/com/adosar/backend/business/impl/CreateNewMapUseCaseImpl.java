package com.adosar.backend.business.impl;

import com.adosar.backend.business.CreateNewMapUseCase;
import com.adosar.backend.business.GetUserByIdUseCase;
import com.adosar.backend.business.converter.MapConverter;
import com.adosar.backend.business.exception.UnauthorizedException;
import com.adosar.backend.business.request.CreateNewMapRequest;
import com.adosar.backend.business.response.CreateNewMapResponse;
import com.adosar.backend.business.service.JWTService;
import com.adosar.backend.domain.Map;
import com.adosar.backend.domain.Removed;
import com.adosar.backend.persistence.MapRepository;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.MapEntity;
import com.adosar.backend.persistence.entity.UserEntity;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class CreateNewMapUseCaseImpl implements CreateNewMapUseCase {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private GetUserByIdUseCase getUserByIdUseCase;
	private MapRepository mapRepository;
	private UserRepository userRepository;

	@Override
	public CreateNewMapResponse createNewMap(@Valid final CreateNewMapRequest request, final String jwt) {
		try {
			// Check if user is authorized
			DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
			if (decodedJWT == null) throw new UnauthorizedException("Unable to decode JWT");
			Integer userId = decodedJWT.getClaim("userId").as(Integer.class);

			// Check if user exists
			UserEntity userEntity = userRepository.getUserEntityByUserId(userId);
			if (userEntity == null)
				throw new UnauthorizedException(String.format("User with ID %s was not found", userId));

			// Create map
			MapEntity mapEntity = MapEntity.builder()
					.user(userEntity)
					.title(request.getTitle())
					.artist(request.getArtist())
					.published(request.getPublished())
					.removed(Removed.NOT_REMOVED)
					.creationDate(Date.from(Instant.now()))
					.lastUpdate(Date.from(Instant.now()))
					.build();

			Map map = MapConverter.convert(mapRepository.saveAndFlush(mapEntity));

			return new CreateNewMapResponse(map, HttpStatus.CREATED);
		} catch (JWTVerificationException | UnauthorizedException jwtVerificationException) {
			LOGGER.log(Level.FINE, jwtVerificationException.toString(), jwtVerificationException);
			return new CreateNewMapResponse(null, HttpStatus.UNAUTHORIZED);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new CreateNewMapResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
