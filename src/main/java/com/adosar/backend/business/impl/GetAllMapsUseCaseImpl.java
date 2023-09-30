package com.adosar.backend.business.impl;

import com.adosar.backend.business.GetAllMapsUseCase;
import com.adosar.backend.business.exception.InvalidPathVariableException;
import com.adosar.backend.business.request.GetAllMapsRequest;
import com.adosar.backend.business.response.GetAllMapsResponse;
import com.adosar.backend.domain.Map;
import com.adosar.backend.persistence.MapRepository;
import com.adosar.backend.persistence.entity.MapEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class GetAllMapsUseCaseImpl implements GetAllMapsUseCase {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private MapRepository mapRepository;

	@Override
	public GetAllMapsResponse getAllMaps(final GetAllMapsRequest request) {
		try {
			if (request.getPage() < 0) throw new InvalidPathVariableException(request.getPage().toString());

			List<MapEntity> result = mapRepository.findAll(PageRequest.of(request.getPage(), 10)).toList();
			List<Map> maps = result.stream().map(MapConverter::convert).toList();

			return new GetAllMapsResponse(maps, HttpStatus.OK);
		} catch (InvalidPathVariableException invalidPathVariableException) {
			LOGGER.log(Level.FINE, invalidPathVariableException.toString(), invalidPathVariableException);
			return new GetAllMapsResponse(null, HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetAllMapsResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
