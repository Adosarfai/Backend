package com.adosar.backend.business;

import com.adosar.backend.business.request.LoginUserRequest;
import com.adosar.backend.business.response.LoginUserResponse;

public interface LoginUserUseCase {
	LoginUserResponse loginUser(LoginUserRequest request);
}
