package com.adosar.backend.business;

import com.adosar.backend.domain.request.GetAllUsersRequest;
import com.adosar.backend.domain.request.GetUserByIdRequest;
import com.adosar.backend.domain.response.GetAllUsersResponse;
import com.adosar.backend.domain.response.GetUserByIdResponse;

public interface GetUserByIdUseCase {
    GetUserByIdResponse getUserById(GetUserByIdRequest request);
}
