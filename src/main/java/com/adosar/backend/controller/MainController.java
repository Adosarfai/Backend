package com.adosar.backend.controller;

import com.adosar.backend.business.GetStatisticsUseCase;
import com.adosar.backend.business.response.GetStatisticsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"https://dev.adosar.net:5173", "https://adosar.net", "https://localhost:5137"})
@RequestMapping(path = "/")
@AllArgsConstructor
@Builder
public class MainController {
	private final GetStatisticsUseCase getStatisticsUseCase;

	@GetMapping(path = "/statistics")
	public @ResponseBody ResponseEntity<GetStatisticsResponse> getStatistics() {
		GetStatisticsResponse response = getStatisticsUseCase.getStatistics();
		if (response.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
			return new ResponseEntity<>(null, response.getHttpStatus());
		}
		return new ResponseEntity<>(response, response.getHttpStatus());
	}
}
