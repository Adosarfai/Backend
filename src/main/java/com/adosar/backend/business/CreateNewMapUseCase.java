package com.adosar.backend.business;

import com.adosar.backend.business.request.CreateNewMapRequest;
import com.adosar.backend.business.response.CreateNewMapResponse;

public interface CreateNewMapUseCase {
    CreateNewMapResponse createNewMap(CreateNewMapRequest request, String jwt);
}
