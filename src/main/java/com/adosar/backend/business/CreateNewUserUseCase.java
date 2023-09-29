package com.adosar.backend.business;

import com.adosar.backend.controller.request.CreateNewUserRequest;
import org.springframework.http.HttpStatus;

public interface CreateNewUserUseCase {
    HttpStatus createNewUser(CreateNewUserRequest request);
}
