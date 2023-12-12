package com.adosar.backend.business.impl;

import com.adosar.backend.business.MapManager;
import com.adosar.backend.business.converter.MapConverter;
import com.adosar.backend.business.exception.NotFoundException;
import com.adosar.backend.business.exception.UnauthorizedException;
import com.adosar.backend.business.request.map.*;
import com.adosar.backend.business.response.map.CreateNewMapResponse;
import com.adosar.backend.business.response.map.GetAllMapsResponse;
import com.adosar.backend.business.response.map.GetMapByIdResponse;
import com.adosar.backend.business.response.map.GetMapsByUserIdResponse;
import com.adosar.backend.business.service.JWTService;
import com.adosar.backend.domain.Map;
import com.adosar.backend.domain.Removed;
import com.adosar.backend.persistence.MapRepository;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.MapEntity;
import com.adosar.backend.persistence.entity.UserEntity;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class MapManagerImpl implements MapManager {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private final MapRepository mapRepository;
	private final UserRepository userRepository;


	@Override
	public CreateNewMapResponse createNewMap(CreateNewMapRequest request, String jwt) {
		try {
			if (request.getTitle().isEmpty()) throw new AssertionError("'title' must have a length of at least 1");
			if (request.getArtist().isEmpty()) throw new AssertionError("'artist' must have a length of at least 1");

			// Check if user is authorized
			DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
			if (decodedJWT == null) throw new UnauthorizedException("Unable to decode JWT");
			Integer userId = decodedJWT.getClaim("userId").as(Integer.class);

			// Check if user exists
			UserEntity userEntity = userRepository.getUserEntityByUserId(userId).orElseThrow(() ->
					new UnauthorizedException(String.format("User with ID %s was not found", userId))
			);

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
		} catch (JWTVerificationException | UnauthorizedException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new CreateNewMapResponse(null, HttpStatus.UNAUTHORIZED);
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new CreateNewMapResponse(null, HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new CreateNewMapResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public GetAllMapsResponse getAllMaps(GetAllMapsRequest request) {
		try {
			if (request.getPage() < 0) throw new AssertionError("'page' must be at least 0");

			// Get maps
			List<MapEntity> result = mapRepository.findAll(PageRequest.of(request.getPage(), 10)).toList();
			List<Map> maps = result.stream().map(MapConverter::convert).toList();

			return new GetAllMapsResponse(maps, HttpStatus.OK);
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetAllMapsResponse(null, HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetAllMapsResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public GetMapByIdResponse getMapById(GetMapByIdRequest request) {
		try {
			if (request.getId() < 1) throw new AssertionError("'id' must be at least 1");

			// Get map
			MapEntity result = mapRepository.findById(request.getId()).orElseThrow(() ->
					new NotFoundException(String.format("Map with ID %s was not found", request.getId()))
			);
			Map map = MapConverter.convert(result);

			return new GetMapByIdResponse(map, HttpStatus.OK);

		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetMapByIdResponse(null, HttpStatus.BAD_REQUEST);
		} catch (NotFoundException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetMapByIdResponse(null, HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetMapByIdResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public GetMapsByUserIdResponse getMapsByUserId(GetMapsByUserIdRequest request) {
		try {
			if (request.getPage() < 0) throw new AssertionError("'page' must be at least 0");
			if (request.getId() < 1) throw new AssertionError("'id' must be at least 1");

			// Get maps
			Collection<MapEntity> result = mapRepository.getMapEntitiesByUser_UserId(request.getId(), PageRequest.of(request.getPage(), 9));
			List<Map> maps = result.stream().map(MapConverter::convert).toList();

			return new GetMapsByUserIdResponse(maps, HttpStatus.OK);
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetMapsByUserIdResponse(null, HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetMapsByUserIdResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public HttpStatus uploadMap(UploadMapRequest request) {
		try {
			if (request.getId() < 1) throw new AssertionError("'id' must be at least 1");

			// Get map
			MapEntity mapEntity = mapRepository.findById(request.getId()).orElseThrow(() ->
					new NotFoundException(String.format("Map with id %s not found", request.getId()))
			);

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
			mapRepository.updateHashByMapId(request.getId(), hash);

			return HttpStatus.OK;
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return HttpStatus.BAD_REQUEST;
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
