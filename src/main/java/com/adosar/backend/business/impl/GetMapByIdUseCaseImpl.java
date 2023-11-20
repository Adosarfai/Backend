package com.adosar.backend.business.impl;

import com.adosar.backend.business.GetMapByIdUseCase;
import com.adosar.backend.business.converter.MapConverter;
import com.adosar.backend.business.exception.BadRequestException;
import com.adosar.backend.business.exception.NotFoundException;
import com.adosar.backend.business.request.GetMapByIdRequest;
import com.adosar.backend.business.response.GetMapByIdResponse;
import com.adosar.backend.domain.Map;
import com.adosar.backend.persistence.MapRepository;
import com.adosar.backend.persistence.entity.MapEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class GetMapByIdUseCaseImpl implements GetMapByIdUseCase {

	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private MapRepository mapRepository;

	@Override
	public GetMapByIdResponse getMapById(GetMapByIdRequest request) {
		try {
			// Check is id is valid
			if (request.getId() < 0) throw new BadRequestException(request.getId().toString());

			// Get map
			MapEntity result = mapRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException(String.format("Map with ID %s was not found", request.getId())));
			Map map = MapConverter.convert(result);

			return new GetMapByIdResponse(map, HttpStatus.OK);

		} catch (BadRequestException badRequestException) {
			LOGGER.log(Level.FINE, badRequestException.toString(), badRequestException);
			return new GetMapByIdResponse(null, HttpStatus.BAD_REQUEST);
		} catch (NotFoundException notFoundException) {
			LOGGER.log(Level.FINE, notFoundException.toString(), notFoundException);
			return new GetMapByIdResponse(null, HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetMapByIdResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
