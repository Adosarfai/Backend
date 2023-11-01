package com.adosar.backend.business;

import com.adosar.backend.business.request.GetUserByIdRequest;
import com.adosar.backend.business.response.GetUserByIdResponse;

public interface GetUserByIdUseCase {
    GetUserByIdResponse getUserById(GetUserByIdRequest request);
}
