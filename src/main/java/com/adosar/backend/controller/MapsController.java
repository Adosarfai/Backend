package com.adosar.backend.controller;

import com.adosar.backend.business.GetAllMapsUseCase;
import com.adosar.backend.business.GetMapByIdUseCase;
import com.adosar.backend.business.request.GetAllMapsRequest;
import com.adosar.backend.business.request.GetMapByIdRequest;
import com.adosar.backend.business.response.GetAllMapsResponse;
import com.adosar.backend.business.response.GetMapByIdResponse;
import com.adosar.backend.domain.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/map")
@AllArgsConstructor
@Builder
public class MapsController {
	private final GetAllMapsUseCase getAllMapsUseCase;
	private final GetMapByIdUseCase getMapByIdUseCase;

	@GetMapping(path = "/all/{page}")
	public @ResponseBody ResponseEntity<Iterable<Map>> getAllMaps(@PathVariable Integer page) {
		GetAllMapsRequest request = new GetAllMapsRequest(page);
		GetAllMapsResponse response = getAllMapsUseCase.getAllMaps(request);
		return new ResponseEntity<>(response.getMaps(), response.getHttpStatus());
	}

	@GetMapping(path = "/{id}")
	public @ResponseBody ResponseEntity<Map> getMapById(@PathVariable Integer id) {
		GetMapByIdRequest request = new GetMapByIdRequest(id);
		GetMapByIdResponse response = getMapByIdUseCase.getMapById(request);
		return new ResponseEntity<>(response.getMap(), response.getHttpStatus());
	}
}
