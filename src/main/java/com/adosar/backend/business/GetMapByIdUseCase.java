package com.adosar.backend.business;

import com.adosar.backend.business.request.GetMapByIdRequest;
import com.adosar.backend.business.response.GetMapByIdResponse;

public interface GetMapByIdUseCase {
    GetMapByIdResponse getMapById(GetMapByIdRequest request);
}
