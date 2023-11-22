package com.adosar.backend.business;

import com.adosar.backend.business.request.GetAllMapsRequest;
import com.adosar.backend.business.request.GetMapsByUserIdRequest;
import com.adosar.backend.business.response.GetAllMapsResponse;
import com.adosar.backend.business.response.GetMapsByUserIdResponse;

public interface GetMapsByUserIdUseCase {
	GetMapsByUserIdResponse getMapsByUserId(GetMapsByUserIdRequest request);
}
