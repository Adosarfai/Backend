package com.adosar.backend.business;

import com.adosar.backend.domain.request.GetAllUsersRequest;
import com.adosar.backend.domain.response.GetAllUsersResponse;

public interface GetAllUsersUseCase {
    GetAllUsersResponse getAllUsers(GetAllUsersRequest request);
}
