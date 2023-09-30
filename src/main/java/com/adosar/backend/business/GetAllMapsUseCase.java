package com.adosar.backend.business;

import com.adosar.backend.business.request.GetAllMapsRequest;
import com.adosar.backend.business.request.GetAllUsersRequest;
import com.adosar.backend.business.response.GetAllMapsResponse;
import com.adosar.backend.business.response.GetAllUsersResponse;

public interface GetAllMapsUseCase {
    GetAllMapsResponse getAllMaps(GetAllMapsRequest request);
}
