package com.adosar.backend.controller;

import com.adosar.backend.business.MapManager;
import com.adosar.backend.business.request.map.*;
import com.adosar.backend.business.response.map.*;
import com.adosar.backend.domain.Map;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Date;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"https://dev.adosar.io:5173", "https://adosar.io", "https://localhost:5137"})
@RequestMapping(path = "/map")
@AllArgsConstructor
@Builder
public class MapController {
	private final MapManager mapManager;

	@GetMapping(path = "/all/{page}")
	public ResponseEntity<Iterable<Map>> getAllMaps(@PathVariable Integer page) {
		GetAllMapsRequest request = new GetAllMapsRequest(page);
		GetAllMapsResponse response = mapManager.getAllMaps(request);
		return new ResponseEntity<>(response.getMaps(), response.getHttpStatus());
	}

	@GetMapping(path = "/user/{id}/{page}")
	public ResponseEntity<Iterable<Map>> getMapsByUserId(@PathVariable Integer id, @PathVariable Integer page) {
		GetMapsByUserIdRequest request = new GetMapsByUserIdRequest(page, id);
		GetMapsByUserIdResponse response = mapManager.getMapsByUserId(request);
		return new ResponseEntity<>(response.getMaps(), response.getHttpStatus());
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<Map> getMapById(@PathVariable Integer id) {
		GetMapByIdRequest request = new GetMapByIdRequest(id);
		GetMapByIdResponse response = mapManager.getMapById(request);
		return new ResponseEntity<>(response.getMap(), response.getHttpStatus());
	}

	@PostMapping
	public ResponseEntity<Map> createNewMap(@RequestBody @Valid CreateNewMapRequest request, @CookieValue(name = "jwt", defaultValue = "") String jwt) {
		CreateNewMapResponse response = mapManager.createNewMap(request, jwt);
		return new ResponseEntity<>(response.getMap(), response.getHttpStatus());
	}

	@PostMapping("/upload")
	public ResponseEntity<HttpStatus> uploadMap(@RequestParam("file") MultipartFile file, @RequestParam("mapId") Integer mapId, @CookieValue(name = "jwt", defaultValue = "") String jwt) {
		UploadMapRequest request = new UploadMapRequest(file, mapId, jwt);
		HttpStatus response = mapManager.uploadMap(request);
		return new ResponseEntity<>(null, response);
	}

	@GetMapping(path = "/query/{page}")
	public ResponseEntity<Iterable<Map>> getMapsByPartialData(@PathVariable Integer page, @RequestParam(value = "title", required = false) String title, @RequestParam(value = "before", required = false) Date before, @RequestParam(value = "after", required = false) Date after) {
		if (title == null || title.isEmpty()) title = "";
		if (before == null) before = Date.from(Instant.now());
		if (after == null) after = Date.from(Instant.EPOCH);
		MapQueryRequest request = MapQueryRequest.builder()
				.title(title)
				.before(before)
				.after(after)
				.page(page)
				.build();
		MapQueryResponse response = mapManager.getMapsByPartialData(request);
		return new ResponseEntity<>(response.getMaps(), response.getHttpStatus());
	}
}
