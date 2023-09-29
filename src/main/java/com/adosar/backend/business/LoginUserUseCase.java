package com.adosar.backend.business;

import com.adosar.backend.controller.request.LoginUserRequest;
import com.adosar.backend.controller.response.LoginUserResponse;

public interface LoginUserUseCase {
    LoginUserResponse loginUser(LoginUserRequest request);
}
