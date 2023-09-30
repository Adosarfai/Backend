package com.adosar.backend.business;

import com.adosar.backend.business.request.GetMapByIdRequest;
import com.adosar.backend.business.request.GetUserByIdRequest;
import com.adosar.backend.business.response.GetMapByIdResponse;
import com.adosar.backend.business.response.GetUserByIdResponse;
import com.adosar.backend.domain.Map;

public interface GetMapByIdUseCase {
    GetMapByIdResponse getMapById(GetMapByIdRequest request);
}
