package com.adosar.backend.business;

import com.adosar.backend.business.request.map.*;
import com.adosar.backend.business.response.map.CreateNewMapResponse;
import com.adosar.backend.business.response.map.GetAllMapsResponse;
import com.adosar.backend.business.response.map.GetMapByIdResponse;
import com.adosar.backend.business.response.map.GetMapsByUserIdResponse;
import org.springframework.http.HttpStatus;

public interface MapManager {
	CreateNewMapResponse createNewMap(CreateNewMapRequest request, String jwt);

	GetAllMapsResponse getAllMaps(GetAllMapsRequest request);

	GetMapByIdResponse getMapById(GetMapByIdRequest request);

	GetMapsByUserIdResponse getMapsByUserId(GetMapsByUserIdRequest request);

	HttpStatus uploadMap(UploadMapRequest request);
}
