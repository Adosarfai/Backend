package com.adosar.backend.business.impl;

import com.adosar.backend.business.converter.MapConverter;
import com.adosar.backend.business.request.map.*;
import com.adosar.backend.business.response.map.CreateNewMapResponse;
import com.adosar.backend.business.response.map.GetAllMapsResponse;
import com.adosar.backend.business.response.map.GetMapByIdResponse;
import com.adosar.backend.business.response.map.GetMapsByUserIdResponse;
import com.adosar.backend.business.service.JWTService;
import com.adosar.backend.domain.Map;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.domain.Removed;
import com.adosar.backend.persistence.MapRepository;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.MapEntity;
import com.adosar.backend.persistence.entity.UserEntity;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.SizeLimitExceededException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SuppressWarnings("UnusedShould")
class MapManagerImplTest {
	// createNewMap method successfully creates a new map with valid input and JWT
	@Test
	void can_create_new_map_with_valid_input_and_jwt() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		CreateNewMapRequest request = CreateNewMapRequest.builder()
				.title("Test Title")
				.artist("Test Artist")
				.published(true)
				.build();
		String jwt = JWTService.createJWT(1);

		UserEntity userEntity = UserEntity.builder()
				.userId(123)
				.build();
		MapEntity mapEntity = MapEntity.builder()
				.user(userEntity)
				.title(request.getTitle())
				.artist(request.getArtist())
				.published(request.getPublished())
				.removed(Removed.NOT_REMOVED)
				.creationDate(Date.from(Instant.now()))
				.lastUpdate(Date.from(Instant.now()))
				.build();

		when(userRepository.getUserEntityByUserId(anyInt())).thenReturn(Optional.of(userEntity));
		when(mapRepository.saveAndFlush(any(MapEntity.class))).thenReturn(mapEntity);

		// Act
		CreateNewMapResponse response = mapManager.createNewMap(request, jwt);

		// Assert
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getHttpStatus());
		assertNotNull(response.getMap());
		assertEquals(request.getTitle(), response.getMap().getTitle());
		assertEquals(request.getArtist(), response.getMap().getArtist());
		assertEquals(request.getPublished(), response.getMap().getPublished());
	}

	// createNewMap method successfully creates a new map with valid input and JWT
	@Test
	void create_new_map_successfully_with_valid_input_and_jwt() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		CreateNewMapRequest request = CreateNewMapRequest.builder()
				.title("Title")
				.artist("Artist")
				.published(true)
				.build();
		String jwt = "valid_jwt";
		Integer userId = 1;
		UserEntity userEntity = UserEntity.builder()
				.userId(1)
				.creationDate(new Date())
				.description("Test")
				.email("test@email.com")
				.badges(Set.of())
				.privilege(Privilege.USER)
				.password("passwordHash")
				.username("test user")
				.build();
		MapEntity mapEntity = MapEntity.builder()
				.user(userEntity)
				.title(request.getTitle())
				.artist(request.getArtist())
				.published(request.getPublished())
				.removed(Removed.NOT_REMOVED)
				.creationDate(Date.from(Instant.now()))
				.lastUpdate(Date.from(Instant.now()))
				.build();
		Map map = MapConverter.convert(mapEntity);
		CreateNewMapResponse expectedResponse = new CreateNewMapResponse(map, HttpStatus.CREATED);

		when(JWTService.verifyJWT(jwt)).thenReturn(mock(DecodedJWT.class));
		when(userRepository.getUserEntityByUserId(userId)).thenReturn(Optional.of(userEntity));
		when(mapRepository.saveAndFlush(any(MapEntity.class))).thenReturn(mapEntity);

		// Act
		CreateNewMapResponse response = mapManager.createNewMap(request, jwt);

		// Assert
		assertEquals(expectedResponse, response);
		verify(JWTService.verifyJWT(jwt));
		verify(userRepository).getUserEntityByUserId(userId);
		verify(mapRepository).saveAndFlush(any(MapEntity.class));
	}

	// getAllMaps method successfully retrieves all maps with valid input
	@Test
	void can_retrieve_all_maps_successfully_with_valid_input() {
		// Arrange
		GetAllMapsRequest request = GetAllMapsRequest.builder()
				.page(0)
				.build();

		List<MapEntity> mapEntities = new ArrayList<>();
		mapEntities.add(MapEntity.builder()
				.mapId(1)
				.user(mock(UserEntity.class))
				.title("Map 1")
				.artist("Artist 1")
				.published(true)
				.removed(Removed.NOT_REMOVED)
				.creationDate(Date.from(Instant.now()))
				.lastUpdate(Date.from(Instant.now()))
				.build());
		mapEntities.add(MapEntity.builder()
				.mapId(2)
				.user(mock(UserEntity.class))
				.title("Map 2")
				.artist("Artist 2")
				.published(true)
				.removed(Removed.NOT_REMOVED)
				.creationDate(Date.from(Instant.now()))
				.lastUpdate(Date.from(Instant.now()))
				.build());

		List<Map> expectedMaps = mapEntities.stream()
				.map(MapConverter::convert)
				.collect(Collectors.toList());

		MapRepository mapRepository = mock(MapRepository.class);
		when(mapRepository.findAll(PageRequest.of(request.getPage(), 10))).thenReturn(new PageImpl<>(mapEntities));

		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, null);

		// Act
		GetAllMapsResponse response = mapManager.getAllMaps(request);

		// Assert
		assertEquals(expectedMaps.toString(), response.getMaps().toString());
		assertEquals(HttpStatus.OK, response.getHttpStatus());
	}

	// getMapById method successfully retrieves a map by ID with valid input
	@Test
	void can_retrieve_map_by_id_with_valid_input() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetMapByIdRequest request = GetMapByIdRequest.builder().id(1).build();
		MapEntity mapEntity = MapEntity.builder()
				.mapId(1)
				.user(mock(UserEntity.class))
				.title("Map Title")
				.artist("Map Artist")
				.published(true)
				.removed(Removed.NOT_REMOVED)
				.creationDate(Date.from(Instant.now()))
				.lastUpdate(Date.from(Instant.now()))
				.build();
		when(mapRepository.findById(1)).thenReturn(Optional.of(mapEntity));

		// Act
		GetMapByIdResponse response = mapManager.getMapById(request);

		// Assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getHttpStatus());
		assertNotNull(response.getMap());
		assertEquals(1, response.getMap().getMapId());
		assertEquals("Map Title", response.getMap().getTitle());
		assertEquals("Map Artist", response.getMap().getArtist());
		assertTrue(response.getMap().getPublished());
		assertEquals(Removed.NOT_REMOVED, response.getMap().getRemoved());
	}

	// getMapsByUserId method successfully retrieves maps by user ID with valid input
	@Test
	void can_retrieve_maps_by_user_id_with_valid_input() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		Integer userId = 1;
		Integer page = 0;
		GetMapsByUserIdRequest request = GetMapsByUserIdRequest.builder()
				.id(userId)
				.page(page)
				.build();
		Collection<MapEntity> mapEntities = new ArrayList<>();
		mapEntities.add(MapEntity.builder()
				.mapId(1)
				.user(mock(UserEntity.class))
				.title("Map 1")
				.artist("Artist 1")
				.published(true)
				.removed(Removed.NOT_REMOVED)
				.creationDate(Date.from(Instant.now()))
				.lastUpdate(Date.from(Instant.now()))
				.build());
		mapEntities.add(MapEntity.builder()
				.mapId(2)
				.user(mock(UserEntity.class))
				.title("Map 2")
				.artist("Artist 2")
				.published(true)
				.removed(Removed.NOT_REMOVED)
				.creationDate(Date.from(Instant.now()))
				.lastUpdate(Date.from(Instant.now()))
				.build());
		when(mapRepository.getMapEntitiesByUser_UserId(userId, PageRequest.of(page, 9))).thenReturn(mapEntities);
		List<Map> expectedMaps = mapEntities.stream().map(MapConverter::convert).collect(Collectors.toList());
		HttpStatus expectedHttpStatus = HttpStatus.OK;

		// Act
		GetMapsByUserIdResponse response = mapManager.getMapsByUserId(request);

		// Assert
		assertEquals(expectedMaps.toString(), response.getMaps().toString());
		assertEquals(expectedHttpStatus, response.getHttpStatus());
	}

	// createNewMap method returns UNAUTHORIZED when JWT is invalid
	@Test
	void createNewMap_returns_UNAUTHORIZED_when_JWT_is_invalid() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		CreateNewMapRequest request = CreateNewMapRequest.builder()
				.title("Test Title")
				.artist("Test Artist")
				.published(true)
				.build();
		String invalidJwt = "invalid_jwt";

		// Act
		CreateNewMapResponse response = mapManager.createNewMap(request, invalidJwt);

		// Assert
		assertNull(response.getMap());
		assertEquals(HttpStatus.UNAUTHORIZED, response.getHttpStatus());
	}

	// createNewMap method returns BAD_REQUEST when title or artist is empty
	@Test
	void createNewMap_returns_BAD_REQUEST_when_title_or_artist_is_empty() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		CreateNewMapRequest request = CreateNewMapRequest.builder()
				.title("")
				.artist("")
				.published(true)
				.build();
		String jwt = "valid_jwt";

		// Act
		CreateNewMapResponse response = mapManager.createNewMap(request, jwt);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
		assertNull(response.getMap());
	}

	// uploadMap method successfully uploads a map file with valid input and JWT
	@Test
	void upload_map_successfully_with_valid_input_and_jwt() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		CreateNewMapRequest createNewMapRequest = CreateNewMapRequest.builder()
				.title("Test Map")
				.artist("Test Artist")
				.published(true)
				.build();
		String jwt = "valid_jwt";
		MultipartFile file = new MockMultipartFile("test.zip", "test.zip", "application/zip", new byte[1024]);
		UploadMapRequest uploadMapRequest = UploadMapRequest.builder()
				.file(file)
				.id(1)
				.jwt(jwt)
				.build();

		UserEntity userEntity = UserEntity.builder()
				.userId(1)
				.creationDate(new Date())
				.description("Test")
				.email("test@email.com")
				.badges(Set.of())
				.privilege(Privilege.USER)
				.password("passwordHash")
				.username("test user")
				.build();

		MapEntity mapEntity = MapEntity.builder()
				.user(userEntity)
				.title(createNewMapRequest.getTitle())
				.artist(createNewMapRequest.getArtist())
				.published(createNewMapRequest.getPublished())
				.removed(Removed.NOT_REMOVED)
				.creationDate(Date.from(Instant.now()))
				.lastUpdate(Date.from(Instant.now()))
				.build();
		Map map = MapConverter.convert(mapEntity);

		when(userRepository.getUserEntityByUserId(anyInt())).thenReturn(Optional.of(userEntity));
		when(mapRepository.saveAndFlush(any(MapEntity.class))).thenReturn(mapEntity);
		when(mapRepository.findById(anyInt())).thenReturn(Optional.of(mapEntity));

		// Act
		CreateNewMapResponse createNewMapResponse = mapManager.createNewMap(createNewMapRequest, jwt);
		HttpStatus uploadMapHttpStatus = mapManager.uploadMap(uploadMapRequest);

		// Assert
		assertNotNull(createNewMapResponse);
		assertEquals(HttpStatus.CREATED, createNewMapResponse.getHttpStatus());
		assertNotNull(createNewMapResponse.getMap());
		assertEquals(map, createNewMapResponse.getMap());

		assertEquals(HttpStatus.OK, uploadMapHttpStatus);

		verify(JWTService.verifyJWT(jwt));
		verify(userRepository).getUserEntityByUserId(anyInt());
		verify(mapRepository).saveAndFlush(any(MapEntity.class));
		verify(mapRepository).findById(anyInt());
	}

	// getAllMaps method returns BAD_REQUEST when page is negative
	@Test
	void test_getAllMaps_returns_BAD_REQUEST_when_page_is_negative() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetAllMapsRequest request = GetAllMapsRequest.builder()
				.page(-1)
				.build();

		// Act
		GetAllMapsResponse response = mapManager.getAllMaps(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
	}

	// getMapById method returns NOT_FOUND when map with ID is not found
	@Test
	void test_getMapById_returns_NOT_FOUND_when_map_with_ID_is_not_found() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetMapByIdRequest request = GetMapByIdRequest.builder().id(1).build();

		// Mock the behavior of mapRepository.findById() to return an empty Optional
		when(mapRepository.findById(request.getId())).thenReturn(Optional.empty());

		// Act
		GetMapByIdResponse response = mapManager.getMapById(request);

		// Assert
		assertNull(response.getMap());
		assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
	}

	// uploadMap method returns NOT_FOUND when map with ID is not found
	@Test
	void upload_map_returns_not_found_when_map_with_id_not_found() {
		// Arrange
		Integer mapId = 1;
		UploadMapRequest request = UploadMapRequest.builder()
				.id(mapId)
				.file(mock(MultipartFile.class))
				.jwt("jwt")
				.build();
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);

		// Mock mapRepository.findById() to return Optional.empty()
		when(mapRepository.findById(mapId)).thenReturn(Optional.empty());

		// Act
		HttpStatus result = mapManager.uploadMap(request);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, result);
	}

	// uploadMap method returns BAD_REQUEST when ID is less than 1
	@Test
	void test_upload_map_returns_bad_request_when_id_is_less_than_1() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		UploadMapRequest request = UploadMapRequest.builder()
				.file(mock(MultipartFile.class))
				.id(0)
				.jwt("jwt")
				.build();

		// Act
		HttpStatus result = mapManager.uploadMap(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, result);
	}

	// uploadMap method returns PAYLOAD_TOO_LARGE when file size exceeds limit
	@Test
	void test_upload_map_returns_payload_too_large_when_file_size_exceeds_limit() throws Exception {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		UploadMapRequest request = UploadMapRequest.builder()
				.file(mock(MultipartFile.class))
				.id(1)
				.jwt("jwt")
				.build();
		when(mapRepository.findById(anyInt())).thenReturn(Optional.of(mock(MapEntity.class)));
		when(request.getFile().getBytes()).thenThrow(SizeLimitExceededException.class);

		// Act
		HttpStatus result = mapManager.uploadMap(request);

		// Assert
		assertEquals(HttpStatus.PAYLOAD_TOO_LARGE, result);
	}

	// uploadMap method returns UNAUTHORIZED when JWT does not match map user ID
	@Test
	void upload_map_returns_unauthorized_when_jwt_does_not_match_map_user_id() {
		// Arrange
		CreateNewMapRequest createNewMapRequest = CreateNewMapRequest.builder()
				.title("Test Title")
				.artist("Test Artist")
				.published(true)
				.build();
		String jwt = "invalid_jwt";
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);

		// Mock user entity
		UserEntity userEntity = UserEntity.builder()
				.userId(1)
				.build();
		when(userRepository.getUserEntityByUserId(anyInt())).thenReturn(Optional.of(userEntity));

		// Act
		CreateNewMapResponse response = mapManager.createNewMap(createNewMapRequest, jwt);

		// Assert
		assertEquals(HttpStatus.UNAUTHORIZED, response.getHttpStatus());
		assertNull(response.getMap());
	}

	// getMapById method returns BAD_REQUEST when ID is less than 1
	@Test
	void test_getMapById_returns_BAD_REQUEST_when_ID_is_less_than_1() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetMapByIdRequest request = GetMapByIdRequest.builder()
				.id(0)
				.build();

		// Act
		GetMapByIdResponse response = mapManager.getMapById(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
		assertNull(response.getMap());
	}

	// getMapById method returns NOT_FOUND when map with the given ID is not found
	@Test
	void test_getMapById_returns_NOT_FOUND_when_map_with_given_ID_is_not_found() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetMapByIdRequest request = new GetMapByIdRequest(1);

		// Act
		when(mapRepository.findById(anyInt())).thenReturn(Optional.empty());
		GetMapByIdResponse response = mapManager.getMapById(request);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
	}

	// getMapById method returns OK and the map when map with the given ID is found
	@Test
	void test_getMapById_returns_OK_and_map_when_map_with_given_ID_is_found() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetMapByIdRequest request = GetMapByIdRequest.builder()
				.id(1)
				.build();
		MapEntity mapEntity = MapEntity.builder()
				.mapId(1)
				.user(mock(UserEntity.class))
				.title("Map 1")
				.artist("Artist 1")
				.published(true)
				.removed(Removed.NOT_REMOVED)
				.creationDate(Date.from(Instant.now()))
				.lastUpdate(Date.from(Instant.now()))
				.build();
		when(mapRepository.findById(1)).thenReturn(Optional.of(mapEntity));

		// Act
		GetMapByIdResponse response = mapManager.getMapById(request);

		// Assert
		assertEquals(HttpStatus.OK, response.getHttpStatus());
		assertNotNull(response.getMap());
		assertEquals(1, response.getMap().getMapId());
		assertEquals("Map 1", response.getMap().getTitle());
		assertEquals("Artist 1", response.getMap().getArtist());
		assertTrue(response.getMap().getPublished());
		assertEquals(Removed.NOT_REMOVED, response.getMap().getRemoved());
	}

	// getMapById method returns INTERNAL_SERVER_ERROR when an exception occurs
	@Test
	void test_getMapById_returns_INTERNAL_SERVER_ERROR_when_exception_occurs() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetMapByIdRequest request = GetMapByIdRequest.builder()
				.id(1)
				.build();
		when(mapRepository.findById(1)).thenThrow(new RuntimeException("Internal Server Error"));

		// Act
		GetMapByIdResponse response = mapManager.getMapById(request);

		// Assert
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
		assertNull(response.getMap());
	}

	// getMapsByUserId method returns BAD_REQUEST when page is negative or ID is less than 1
	@Test
	void test_getMapsByUserId_returns_BAD_REQUEST_when_page_is_negative() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetMapsByUserIdRequest request = GetMapsByUserIdRequest.builder()
				.page(-1)
				.id(1)
				.build();

		// Act
		GetMapsByUserIdResponse response = mapManager.getMapsByUserId(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
		assertNull(response.getMaps());
	}

	// getMapsByUserId method returns BAD_REQUEST when ID is less than 1
	@Test
	void test_getMapsByUserId_returns_BAD_REQUEST_when_ID_is_less_than_1() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetMapsByUserIdRequest request = GetMapsByUserIdRequest.builder()
				.page(0)
				.id(0)
				.build();

		// Act
		GetMapsByUserIdResponse response = mapManager.getMapsByUserId(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
		assertNull(response.getMaps());
	}

	// getMapsByUserId method returns OK when valid request is provided
	@Test
	void test_getMapsByUserId_returns_OK_when_valid_request_is_provided() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetMapsByUserIdRequest request = GetMapsByUserIdRequest.builder()
				.page(0)
				.id(1)
				.build();
		List<MapEntity> mapEntities = List.of(
				mock(MapEntity.class),
				mock(MapEntity.class)
		);
		when(mapRepository.getMapEntitiesByUser_UserId(eq(1), any(PageRequest.class))).thenReturn(mapEntities);

		// Act
		GetMapsByUserIdResponse response = mapManager.getMapsByUserId(request);

		// Assert
		assertEquals(HttpStatus.OK, response.getHttpStatus());
		assertEquals(mapEntities.size(), StreamSupport.stream(response.getMaps().spliterator(), false).count());
	}

	// getMapsByUserId method returns INTERNAL_SERVER_ERROR when an exception occurs
	@Test
	void test_getMapsByUserId_returns_INTERNAL_SERVER_ERROR_when_exception_occurs() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetMapsByUserIdRequest request = GetMapsByUserIdRequest.builder()
				.page(0)
				.id(1)
				.build();
		when(mapRepository.getMapEntitiesByUser_UserId(eq(1), any(PageRequest.class))).thenThrow(new RuntimeException());

		// Act
		GetMapsByUserIdResponse response = mapManager.getMapsByUserId(request);

		// Assert
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
		assertNull(response.getMaps());
	}

	// getMapsByUserId method returns BAD_REQUEST when page is negative and ID is less than 1
	@Test
	void test_getMapsByUserId_returns_BAD_REQUEST_when_page_is_negative_and_ID_is_less_than_1() {
		// Arrange
		MapRepository mapRepository = mock(MapRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		MapManagerImpl mapManager = new MapManagerImpl(mapRepository, userRepository);
		GetMapsByUserIdRequest request = new GetMapsByUserIdRequest(-1, 0);

		// Act
		HttpStatus result = mapManager.getMapsByUserId(request).getHttpStatus();

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, result);
	}
}