package com.adosar.backend.business;

import com.adosar.backend.business.request.RemoveUserRequest;
import org.springframework.http.HttpStatus;

public interface RemoveUserUseCase {
    HttpStatus RemoveUser(RemoveUserRequest request);
}
