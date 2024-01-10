package com.adosar.backend.business;

import com.adosar.backend.business.request.map.*;
import com.adosar.backend.business.response.map.*;
import org.springframework.http.HttpStatus;

public interface MapManager {
	CreateNewMapResponse createNewMap(CreateNewMapRequest request, String jwt);

	GetAllMapsResponse getAllMaps(GetAllMapsRequest request);

	GetMapByIdResponse getMapById(GetMapByIdRequest request);

	GetMapsByUserIdResponse getMapsByUserId(GetMapsByUserIdRequest request);

	MapQueryResponse getMapsByPartialData(MapQueryRequest request);

	HttpStatus uploadMap(UploadMapRequest request);
}
