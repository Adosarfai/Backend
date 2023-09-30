package com.adosar.backend.business;

import com.adosar.backend.business.request.ActivateUserRequest;
import org.springframework.http.HttpStatus;

public interface ActivateUserUseCase {
    HttpStatus ActivateUser(ActivateUserRequest request);
}
