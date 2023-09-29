package com.adosar.backend.business;

import com.adosar.backend.controller.request.GetUserByIdRequest;
import com.adosar.backend.controller.response.GetUserByIdResponse;

public interface GetUserByIdUseCase {
    GetUserByIdResponse getUserById(GetUserByIdRequest request);
}
