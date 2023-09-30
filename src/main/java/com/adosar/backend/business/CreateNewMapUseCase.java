package com.adosar.backend.business;

import com.adosar.backend.business.request.CreateNewMapRequest;
import com.adosar.backend.business.request.CreateNewUserRequest;
import com.adosar.backend.business.response.CreateNewMapResponse;
import org.springframework.http.HttpStatus;

public interface CreateNewMapUseCase {
	CreateNewMapResponse createNewMap(CreateNewMapRequest request, String jwt);
}
