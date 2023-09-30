package com.adosar.backend.controller;

import com.adosar.backend.business.CreateNewMapUseCase;
import com.adosar.backend.business.GetAllMapsUseCase;
import com.adosar.backend.business.GetMapByIdUseCase;
import com.adosar.backend.business.request.CreateNewMapRequest;
import com.adosar.backend.business.request.GetAllMapsRequest;
import com.adosar.backend.business.request.GetMapByIdRequest;
import com.adosar.backend.business.response.CreateNewMapResponse;
import com.adosar.backend.business.response.GetAllMapsResponse;
import com.adosar.backend.business.response.GetMapByIdResponse;
import com.adosar.backend.domain.Map;
import jakarta.validation.Valid;
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
	private final CreateNewMapUseCase createNewMapUseCase;

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
	
	@PostMapping
	public @ResponseBody ResponseEntity<Map> createNewMap(@RequestBody @Valid CreateNewMapRequest request, @CookieValue(name = "jwt", defaultValue = "") String jwt) {
		CreateNewMapResponse response = createNewMapUseCase.createNewMap(request, jwt);
		return new ResponseEntity<>(response.getMap(), response.getHttpStatus());
	}
}
