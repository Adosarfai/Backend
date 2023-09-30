package com.adosar.backend.business.impl;

import com.adosar.backend.business.GetMapByIdUseCase;
import com.adosar.backend.business.exception.FieldNotFoundException;
import com.adosar.backend.business.exception.InvalidPathVariableException;
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
			if (request.getId() < 0) throw new InvalidPathVariableException(request.getId().toString());

			MapEntity result = mapRepository.findById(request.getId()).orElseThrow(() -> new FieldNotFoundException(String.format("Map with ID %s was not found", request.getId())));
			Map map = MapConverter.convert(result);

			return new GetMapByIdResponse(map, HttpStatus.OK);

		} catch (InvalidPathVariableException invalidPathVariableException) {
			LOGGER.log(Level.FINE, invalidPathVariableException.toString(), invalidPathVariableException);
			return new GetMapByIdResponse(null, HttpStatus.BAD_REQUEST);
		} catch (FieldNotFoundException fieldNotFoundException) {
			LOGGER.log(Level.FINE, fieldNotFoundException.toString(), fieldNotFoundException);
			return new GetMapByIdResponse(null, HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetMapByIdResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
