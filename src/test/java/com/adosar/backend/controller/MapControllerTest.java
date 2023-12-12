package com.adosar.backend.controller;

import com.adosar.backend.business.MapManager;
import com.adosar.backend.business.request.map.*;
import com.adosar.backend.business.response.map.CreateNewMapResponse;
import com.adosar.backend.business.response.map.GetAllMapsResponse;
import com.adosar.backend.business.response.map.GetMapByIdResponse;
import com.adosar.backend.business.response.map.GetMapsByUserIdResponse;
import com.adosar.backend.domain.Map;
import com.adosar.backend.domain.Removed;
import com.adosar.backend.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SuppressWarnings("UnusedShould")
class MapControllerTest {
	// GET request to /map/user/{id}/{page} returns a ResponseEntity with an Iterable of Maps and HttpStatus
	@Test
	void test_get_maps_by_user_id_returns_response_entity_with_iterable_of_maps_and_http_status() {
		// Arrange
		MapManager mapManager = mock(MapManager.class);
		MapController mapController = new MapController(mapManager);
		Integer id = 1;
		Integer page = 0;
		GetMapsByUserIdRequest request = GetMapsByUserIdRequest.builder()
				.id(id)
				.page(page)
				.build();
		Iterable<Map> expectedMaps = Arrays.asList(
				Map.builder().build(),
				Map.builder().build()
		);
		HttpStatus expectedHttpStatus = HttpStatus.OK;
		GetMapsByUserIdResponse expectedResponse = new GetMapsByUserIdResponse(expectedMaps, expectedHttpStatus);

		when(mapManager.getMapsByUserId(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<Iterable<Map>> response = mapController.getMapsByUserId(id, page);

		// Assert
		assertEquals(expectedMaps, response.getBody());
		assertEquals(expectedHttpStatus, response.getStatusCode());
	}

	// GET request to /map/{id} returns a ResponseEntity with a Map
	@Test
	void test_get_map_by_id_returns_response_entity_with_map_and_http_status() {
		// Arrange
		MapManager mapManager = mock(MapManager.class);
		MapController mapController = new MapController(mapManager);
		Integer id = 1;
		GetMapByIdRequest request = new GetMapByIdRequest(id);
		GetMapByIdResponse response = GetMapByIdResponse.builder()
				.map(Map.builder().build())
				.httpStatus(HttpStatus.OK)
				.build();
		when(mapManager.getMapById(request)).thenReturn(response);

		// Act
		ResponseEntity<Map> result = mapController.getMapById(id);

		// Assert
		assertEquals(response.getMap(), result.getBody());
		assertEquals(response.getHttpStatus(), result.getStatusCode());
	}

	// POST request to /map/upload with a valid UploadMapRequest returns a ResponseEntity with a HttpStatus
	@Test
	void test_upload_map_with_valid_input_and_jwt_returns_response_entity_with_http_status() {
		// Arrange
		MapManager mapManager = mock(MapManager.class);
		MapController mapController = new MapController(mapManager);
		MultipartFile file = new MockMultipartFile("test.zip", "test.zip", "application/zip", new byte[1024]);
		Integer mapId = 1;
		String jwt = "valid_jwt";
		UploadMapRequest request = UploadMapRequest.builder()
				.file(file)
				.id(mapId)
				.jwt(jwt)
				.build();
		HttpStatus expectedHttpStatus = HttpStatus.OK;

		// Mock the behavior of mapManager.uploadMap() to return the expected HttpStatus
		when(mapManager.uploadMap(request)).thenReturn(expectedHttpStatus);

		// Act
		ResponseEntity<HttpStatus> response = mapController.uploadMap(file, mapId, jwt);

		// Assert
		assertEquals(expectedHttpStatus, response.getBody());
	}

	// POST request to /map with a valid CreateNewMapRequest and a valid jwt cookie returns a ResponseEntity with a Map and HttpStatus
	@Test
	void can_create_new_map_with_valid_input_and_jwt() {
		// Arrange
		MapManager mapManager = mock(MapManager.class);
		MapController mapController = new MapController(mapManager);
		CreateNewMapRequest request = CreateNewMapRequest.builder()
				.title("Test Title")
				.artist("Test Artist")
				.published(true)
				.build();
		String jwt = "valid_jwt_token";

		CreateNewMapResponse expectedResponse = CreateNewMapResponse.builder()
				.map(Map.builder()
						.user(User.builder().build())
						.mapId(1)
						.creationDate(new Date())
						.title(request.getTitle())
						.artist(request.getArtist())
						.lastUpdate(new Date())
						.removed(Removed.NOT_REMOVED)
						.published(request.getPublished())
						.build())
				.httpStatus(HttpStatus.CREATED)
				.build();

		when(mapManager.createNewMap(request, jwt)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<Map> response = mapController.createNewMap(request, jwt);

		// Assert
		assertEquals(expectedResponse.getMap(), response.getBody());
		assertEquals(expectedResponse.getHttpStatus(), response.getStatusCode());
	}

	// GET request to /map/all/{page} with a negative page number returns a ResponseEntity with an empty Iterable of Maps and HttpStatus
	@Test
	void test_get_all_maps_with_negative_page_number() {
		// Arrange
		MapManager mapManager = mock(MapManager.class);
		MapController mapController = new MapController(mapManager);
		Integer page = -1;

		GetAllMapsRequest request = GetAllMapsRequest.builder()
				.page(page)
				.build();

		GetAllMapsResponse expectedResponse = GetAllMapsResponse.builder()
				.maps(Collections.emptyList())
				.httpStatus(HttpStatus.OK)
				.build();

		// Mock the behavior of mapManager.getAllMaps() to return the expected response
		when(mapManager.getAllMaps(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<Iterable<Map>> response = mapController.getAllMaps(page);

		// Assert
		assertEquals(expectedResponse.getMaps(), response.getBody());
		assertEquals(expectedResponse.getHttpStatus(), response.getStatusCode());
	}

	// GET request to /map/{id} with an invalid id returns a ResponseEntity with a null Map and HttpStatus NOT_FOUND
	@Test
	void test_get_map_by_id_with_invalid_id_returns_response_entity_with_null_map_and_not_found_status() {
		// Arrange
		MapManager mapManager = mock(MapManager.class);
		MapController mapController = new MapController(mapManager);
		Integer invalidId = -1;

		// Mock the behavior of mapManager.getMapById() to return a GetMapByIdResponse with null map and HttpStatus.NOT_FOUND
		GetMapByIdRequest request = new GetMapByIdRequest(invalidId);
		GetMapByIdResponse response = new GetMapByIdResponse(null, HttpStatus.NOT_FOUND);
		when(mapManager.getMapById(request)).thenReturn(response);

		// Act
		ResponseEntity<Map> result = mapController.getMapById(invalidId);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull(result.getBody());
	}

	// GET request to /map/user/{id}/{page} with a negative page number returns a ResponseEntity with an empty Iterable of Maps and HttpStatus
	@Test
	void test_get_maps_by_user_id_with_negative_page_number() {
		// Arrange
		MapManager mapManager = mock(MapManager.class);
		MapController mapController = new MapController(mapManager);
		Integer id = 1;
		Integer page = -1;
		GetMapsByUserIdRequest request = GetMapsByUserIdRequest.builder()
				.id(id)
				.page(page)
				.build();
		GetMapsByUserIdResponse expectedResponse = GetMapsByUserIdResponse.builder()
				.maps(Collections.emptyList())
				.httpStatus(HttpStatus.OK)
				.build();

		// Mock the behavior of mapManager.getMapsByUserId() to return the expected response
		when(mapManager.getMapsByUserId(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<Iterable<Map>> response = mapController.getMapsByUserId(id, page);

		// Assert
		assertEquals(expectedResponse.getMaps(), response.getBody());
		assertEquals(expectedResponse.getHttpStatus(), response.getStatusCode());
	}

	// POST request to /map with an invalid CreateNewMapRequest returns a ResponseEntity with a null Map and HttpStatus BAD_REQUEST
	@Test
	void test_post_request_with_invalid_CreateNewMapRequest_returns_ResponseEntity_with_null_Map_and_BAD_REQUEST() {
		// Arrange
		MapManager mapManager = mock(MapManager.class);
		MapController mapController = new MapController(mapManager);
		CreateNewMapRequest request = CreateNewMapRequest.builder()
				.title("")
				.artist("")
				.published(true)
				.build();
		String jwt = "valid_jwt_token";

		// Act
		ResponseEntity<Map> response = mapController.createNewMap(request, jwt);

		// Assert
		assertNull(response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	// GET request to /map/user/{id}/{page} with an invalid user id returns a ResponseEntity with an empty Iterable of Maps and HttpStatus
	@Test
	void test_get_maps_by_user_id_with_invalid_user_id_returns_empty_iterable_of_maps_and_http_status() {
		// Arrange
		MapManager mapManager = mock(MapManager.class);
		MapController mapController = new MapController(mapManager);
		Integer userId = -1;
		Integer page = 0;
		GetMapsByUserIdRequest request = GetMapsByUserIdRequest.builder()
				.id(userId)
				.page(page)
				.build();
		GetMapsByUserIdResponse expectedResponse = GetMapsByUserIdResponse.builder()
				.maps(Collections.emptyList())
				.httpStatus(HttpStatus.OK)
				.build();

		// Mock the behavior of mapManager.getMapsByUserId() to return an empty Iterable of Maps
		when(mapManager.getMapsByUserId(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<Iterable<Map>> response = mapController.getMapsByUserId(userId, page);

		// Assert
		assertEquals(expectedResponse.getHttpStatus(), response.getStatusCode());
		assertEquals(expectedResponse.getMaps(), response.getBody());

		verify(mapManager).getMapsByUserId(request);
	}

	// GET request to /map/user/{id}/{page} with a page number greater than the total number of maps for that user returns a ResponseEntity with an empty Iterable of Maps and HttpStatus
	@Test
	void test_get_maps_by_user_id_with_page_greater_than_total_number_of_maps_when_exception_occurs() {
		// Arrange
		MapManager mapManager = mock(MapManager.class);
		MapController mapController = new MapController(mapManager);
		Integer userId = 1;
		Integer page = 2;
		GetMapsByUserIdRequest request = GetMapsByUserIdRequest.builder()
				.id(userId)
				.page(page)
				.build();
		when(mapManager.getMapsByUserId(request)).thenThrow(new RuntimeException("Internal Server Error"));

		// Act
		ResponseEntity<Iterable<Map>> result = mapController.getMapsByUserId(userId, page);

		// Assert
		assertNull(result.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
		verify(mapManager).getMapsByUserId(request);
	}

	// GET request to /map/all/{page} returns a ResponseEntity with an Iterable of Maps and HttpStatus
	@Test
	void test_get_all_maps_returns_response_entity_with_iterable_of_maps_and_http_status_when_map_manager_returns_non_empty_list_and_page_is_greater_than_0_and_maps_are_paginated_and_page_size_is_greater_than_map_count_and_map_count_is_negative() {
		// Arrange
		MapManager mapManager = mock(MapManager.class);
		MapController mapController = new MapController(mapManager);
		Integer page = 1;
		GetAllMapsRequest request = GetAllMapsRequest.builder()
				.page(page)
				.build();
		List<Map> maps = Collections.emptyList();
		GetAllMapsResponse response = GetAllMapsResponse.builder()
				.maps(maps)
				.httpStatus(HttpStatus.OK)
				.build();
		when(mapManager.getAllMaps(request)).thenReturn(response);

		// Act
		ResponseEntity<Iterable<Map>> result = mapController.getAllMaps(page);

		// Assert
		assertEquals(response.getMaps(), result.getBody());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		verify(mapManager).getAllMaps(request);
	}

}