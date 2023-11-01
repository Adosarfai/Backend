package com.adosar.backend.business;

import com.adosar.backend.business.request.GetAllUsersRequest;
import com.adosar.backend.business.response.GetAllUsersResponse;

public interface GetAllUsersUseCase {
    GetAllUsersResponse getAllUsers(GetAllUsersRequest request);
}
