package com.adosar.backend.business.impl;

import com.adosar.backend.business.CreateNewMapUseCase;
import com.adosar.backend.business.CreateNewUserUseCase;
import com.adosar.backend.business.GetUserByIdUseCase;
import com.adosar.backend.business.exception.FieldNotFoundException;
import com.adosar.backend.business.exception.InvalidPathVariableException;
import com.adosar.backend.business.request.CreateNewMapRequest;
import com.adosar.backend.business.request.CreateNewUserRequest;
import com.adosar.backend.business.request.GetUserByIdRequest;
import com.adosar.backend.business.response.CreateNewMapResponse;
import com.adosar.backend.business.response.GetUserByIdResponse;
import com.adosar.backend.domain.Map;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.domain.Removed;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.MapRepository;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.MapEntity;
import com.adosar.backend.persistence.entity.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.password4j.BadParametersException;
import com.password4j.Hash;
import com.password4j.Password;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
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

	@Override
	public CreateNewMapResponse createNewMap(@Valid final CreateNewMapRequest request, final String jwt) {
		try {
			Algorithm algorithm = Algorithm.HMAC512(System.getenv("HMAC512_SECRET"));
			JWTVerifier verifier = JWT.require(algorithm)
					                       .withIssuer("Adosar")
					                       .withSubject("User auth")
					                       .build();
			DecodedJWT decodedJWT = verifier.verify(jwt);

			Integer userId = decodedJWT.getClaim("UserId").as(Integer.class);
			GetUserByIdResponse getUserByIdResponse = getUserByIdUseCase.getUserById(new GetUserByIdRequest(userId));
			if (!getUserByIdResponse.getHttpStatus().is2xxSuccessful() || getUserByIdResponse.getUser() == null) throw new FieldNotFoundException(String.format("User with ID %s was not found", userId));

			MapEntity mapEntity = MapEntity.builder()
					.user(UserConverter.convert(getUserByIdResponse.getUser()))
					.title(request.getTitle())
					.artist(request.getArtist())
					.published(request.getPublished())
					.removed(Removed.NOT_REMOVED)
					.creationDate(Date.from(Instant.now()))
					.lastUpdate(Date.from(Instant.now()))
					.build();
			
			Map map = MapConverter.convert(mapRepository.saveAndFlush(mapEntity));

			return new CreateNewMapResponse(map, HttpStatus.CREATED);
		} catch (JWTVerificationException | FieldNotFoundException jwtVerificationException) {
			LOGGER.log(Level.FINE, jwtVerificationException.toString(), jwtVerificationException);
			return new CreateNewMapResponse(null, HttpStatus.UNAUTHORIZED);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new CreateNewMapResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
