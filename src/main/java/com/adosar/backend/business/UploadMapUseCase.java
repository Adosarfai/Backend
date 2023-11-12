package com.adosar.backend.business;

import com.adosar.backend.business.request.UploadMapRequest;
import org.springframework.http.HttpStatus;

public interface UploadMapUseCase {
	HttpStatus uploadMap(UploadMapRequest request);
}
