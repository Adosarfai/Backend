package com.adosar.backend.business.impl;

import com.adosar.backend.business.GetAllMapsUseCase;
import com.adosar.backend.business.GetMapsByUserIdUseCase;
import com.adosar.backend.business.converter.MapConverter;
import com.adosar.backend.business.exception.BadRequestException;
import com.adosar.backend.business.request.GetAllMapsRequest;
import com.adosar.backend.business.request.GetMapsByUserIdRequest;
import com.adosar.backend.business.response.GetAllMapsResponse;
import com.adosar.backend.business.response.GetMapsByUserIdResponse;
import com.adosar.backend.domain.Map;
import com.adosar.backend.persistence.MapRepository;
import com.adosar.backend.persistence.entity.MapEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class GetMapsByUserIdUseCaseImpl implements GetMapsByUserIdUseCase {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private MapRepository mapRepository;

	@Override
	public GetMapsByUserIdResponse getMapsByUserId(final GetMapsByUserIdRequest request) {
		try {
			// Check is page & id are valid
			if (request.getPage() < 0) throw new BadRequestException(request.getPage().toString());
			if (request.getUserId() < 0) throw new BadRequestException(request.getUserId().toString());
			
			// Get maps
			Collection<MapEntity> result = mapRepository.getMapEntitiesByUser_UserId(request.getUserId(), PageRequest.of(request.getPage(), 9));
			List<Map> maps = result.stream().map(MapConverter::convert).toList();

			return new GetMapsByUserIdResponse(maps, HttpStatus.OK);
		} catch (BadRequestException badRequestException) {
			LOGGER.log(Level.FINE, badRequestException.toString(), badRequestException);
			return new GetMapsByUserIdResponse(null, HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetMapsByUserIdResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
