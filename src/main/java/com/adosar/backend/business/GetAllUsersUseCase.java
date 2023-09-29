package com.adosar.backend.business;

import com.adosar.backend.controller.request.GetAllUsersRequest;
import com.adosar.backend.controller.response.GetAllUsersResponse;

public interface GetAllUsersUseCase {
    GetAllUsersResponse getAllUsers(GetAllUsersRequest request);
}
