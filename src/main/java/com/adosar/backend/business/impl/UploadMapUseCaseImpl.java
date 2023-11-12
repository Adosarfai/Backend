package com.adosar.backend.business.impl;

import com.adosar.backend.business.UploadMapUseCase;
import com.adosar.backend.business.exception.NotFoundException;
import com.adosar.backend.business.exception.UnauthorizedException;
import com.adosar.backend.business.request.UploadMapRequest;
import com.adosar.backend.business.service.JWTService;
import com.adosar.backend.domain.Map;
import com.adosar.backend.persistence.MapRepository;
import com.adosar.backend.persistence.entity.MapEntity;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.hash.Hashing;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class UploadMapUseCaseImpl implements UploadMapUseCase {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private MapRepository mapRepository;

	@Override
	public HttpStatus uploadMap(@Valid final UploadMapRequest request) {
		try {
			// Get map
			MapEntity mapEntity = mapRepository.getMapEntityByMapId(request.getMapId());
			if (mapEntity == null)
				throw new NotFoundException(String.format("Map with id %s not found", request.getMapId()));

			Map map = MapConverter.convert(mapEntity);

			// Check if user is authorized
			DecodedJWT decodedJWT = JWTService.verifyJWT(request.getJwt());
			if (decodedJWT == null) throw new UnauthorizedException("Unable to decode JWT");
			if (!decodedJWT.getClaim("userId").as(Integer.class).equals(map.getUser().getUserId()))
				throw new UnauthorizedException("JWT and userId do not match");

			// Save map
			String mapLocation = String.format("%s\\%s.zip", System.getenv("DOWNLOAD_LOCATION"), map.getMapId());
			request.getFile().transferTo(Path.of(mapLocation));

			// Update map hash
			String hash = Hashing.sha256().hashBytes(request.getFile().getBytes()).toString();
			mapRepository.updateHashByMapId(request.getMapId(), hash);

			return HttpStatus.OK;
		} catch (NotFoundException notFoundException) {
			LOGGER.log(Level.FINE, notFoundException.toString(), notFoundException);
			return HttpStatus.NOT_FOUND;
		} catch (UnauthorizedException unauthorizedException) {
			LOGGER.log(Level.FINE, unauthorizedException.toString(), unauthorizedException);
			return HttpStatus.UNAUTHORIZED;
		} catch (SizeLimitExceededException sizeLimitExceededException) {
			LOGGER.log(Level.FINE, sizeLimitExceededException.toString(), sizeLimitExceededException);
			return HttpStatus.PAYLOAD_TOO_LARGE;
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
