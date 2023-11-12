package com.adosar.backend.business;

import com.adosar.backend.business.request.GetAllMapsRequest;
import com.adosar.backend.business.response.GetAllMapsResponse;

public interface GetAllMapsUseCase {
	GetAllMapsResponse getAllMaps(GetAllMapsRequest request);
}
