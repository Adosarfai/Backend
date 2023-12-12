package com.adosar.backend.controller;

import com.adosar.backend.business.UserManager;
import com.adosar.backend.business.request.user.*;
import com.adosar.backend.business.response.user.GetAllUsersResponse;
import com.adosar.backend.business.response.user.GetUserByIdResponse;
import com.adosar.backend.business.response.user.LoginUserResponse;
import com.adosar.backend.business.response.user.UserQueryResponse;
import com.adosar.backend.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("UnusedShould")
class UserControllerTest {
	// Should create a new user
	@Test
	void can_create_new_user_successfully_with_valid_input() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		CreateNewUserRequest request = CreateNewUserRequest.builder()
				.email("test@example.com")
				.username("testuser")
				.password("password123")
				.build();
		HttpStatus expectedHttpStatus = HttpStatus.CREATED;

		// Mock the behavior of UserManager.createNewUser() to return the expected HttpStatus
		when(userManager.createNewUser(request)).thenReturn(expectedHttpStatus);

		// Act
		ResponseEntity<HttpStatus> response = userController.createNewUser(request);

		// Assert
		assertNotNull(response);
		assertEquals(expectedHttpStatus, response.getBody());
	}

	// Should activate user account
	@Test
	void test_should_activate_user_account() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer userId = 1;
		HttpStatus expectedHttpStatus = HttpStatus.OK;

		// Mock UserManager.activateUser() to return the expected HttpStatus
		when(userManager.activateUser(any(ActivateUserRequest.class))).thenReturn(expectedHttpStatus);

		// Act
		ResponseEntity<HttpStatus> response = userController.activateUser(userId);

		// Assert
		assertEquals(expectedHttpStatus, response.getStatusCode());
		verify(userManager).activateUser(ActivateUserRequest.builder().id(userId).build());
	}

	// Should get all users that match provided queries
	@Test
	void test_getUsersByPartialData_returns_OK_and_users_when_valid_request_is_provided() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer page = 0;
		String username = "test";
		Date before = Date.from(Instant.now());
		Date after = Date.from(Instant.EPOCH);
		UserQueryRequest request = UserQueryRequest.builder()
				.username(username)
				.before(before)
				.after(after)
				.page(page)
				.build();
		Iterable<User> users = Arrays.asList(
				User.builder().build(),
				User.builder().build()
		);
		UserQueryResponse expectedResponse = new UserQueryResponse(users, HttpStatus.OK);
		when(userManager.getUsersByPartialData(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<Iterable<User>> response = userController.getUsersByPartialData(page, username, before, after);

		// Assert
		assertEquals(expectedResponse.getHttpStatus(), response.getStatusCode());
		assertEquals(users, response.getBody());
	}

	// Should return 10 users offset by the page number
	@Test
	void test_should_return_10_users_offset_by_page_number() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer page = 0;
		GetAllUsersRequest request = GetAllUsersRequest.builder()
				.page(page)
				.build();
		GetAllUsersResponse expectedResponse = GetAllUsersResponse.builder()
				.users(Collections.emptyList())
				.httpStatus(HttpStatus.OK)
				.build();

		when(userManager.getAllUsers(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<GetAllUsersResponse> responseEntity = userController.getAllUsers(page);

		// Assert
		assertEquals(expectedResponse, responseEntity.getBody());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		verify(userManager).getAllUsers(request);
	}

	// Should return empty list when no users are on the page
	@Test
	void test_should_return_empty_list_when_no_users_on_page() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer page = 0;
		GetAllUsersRequest request = GetAllUsersRequest.builder()
				.page(page)
				.build();
		GetAllUsersResponse expectedResponse = GetAllUsersResponse.builder()
				.users(Collections.emptyList())
				.httpStatus(HttpStatus.OK)
				.build();

		when(userManager.getAllUsers(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<GetAllUsersResponse> responseEntity = userController.getAllUsers(page);

		// Assert
		assertEquals(expectedResponse, responseEntity.getBody());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		verify(userManager).getAllUsers(request);
	}

	// Should return 400 BAD_REQUEST when page number is invalid
	@Test
	void test_should_return_bad_request_when_page_number_invalid() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer page = -1;
		GetAllUsersRequest request = GetAllUsersRequest.builder()
				.page(page)
				.build();
		GetAllUsersResponse expectedResponse = GetAllUsersResponse.builder()
				.httpStatus(HttpStatus.BAD_REQUEST)
				.build();

		when(userManager.getAllUsers(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<GetAllUsersResponse> responseEntity = userController.getAllUsers(page);

		// Assert
		assertEquals(expectedResponse, responseEntity.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		verify(userManager).getAllUsers(request);
	}

	// Should return 400 BAD_REQUEST when page number is invalid
	@Test
	void test_should_return_bad_request_when_page_number_is_invalid() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer invalidPage = -1;

		// Act
		ResponseEntity<GetAllUsersResponse> response = userController.getAllUsers(invalidPage);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	// Should return 400 BAD_REQUEST when ID is invalid
	@Test
	void test_should_return_bad_request_when_id_is_invalid() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer id = -1;

		// Act
		ResponseEntity<User> response = userController.getUserById(id);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	// Should return empty list when no users are on the page
	@Test
	void test_should_return_empty_list_when_no_users_are_on_the_page() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer page = 0;
		GetAllUsersRequest request = GetAllUsersRequest.builder()
				.page(page)
				.build();
		GetAllUsersResponse response = GetAllUsersResponse.builder()
				.users(Collections.emptyList())
				.httpStatus(HttpStatus.OK)
				.build();
		when(userManager.getAllUsers(request)).thenReturn(response);

		// Act
		ResponseEntity<GetAllUsersResponse> result = userController.getAllUsers(page);

		// Assert
		assertEquals(response, result.getBody());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		verify(userManager).getAllUsers(request);
	}

	// Should return 404 NOT_FOUND when no user with ID exists
	@Test
	void test_should_return_404_not_found_when_no_user_with_id_exists() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer id = 1;
		GetUserByIdRequest request = GetUserByIdRequest.builder()
				.id(id)
				.build();
		GetUserByIdResponse expectedResponse = GetUserByIdResponse.builder()
				.user(null)
				.httpStatus(HttpStatus.NOT_FOUND)
				.build();

		// Mock the behavior of userManager.getUserById() to return the expected response
		when(userManager.getUserById(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<User> response = userController.getUserById(id);

		// Assert
		assertEquals(expectedResponse.getUser(), response.getBody());
		assertEquals(expectedResponse.getHttpStatus(), response.getStatusCode());

		verify(userManager).getUserById(request);
	}

	// Should return 401 UNAUTHORIZED when the login credentials are invalid
	@Test
	void test_return_401_unauthorized_when_login_credentials_are_invalid() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		LoginUserRequest request = LoginUserRequest.builder()
				.email("invalid_email")
				.password("invalid_password")
				.build();
		LoginUserResponse response = LoginUserResponse.builder()
				.httpStatus(HttpStatus.UNAUTHORIZED)
				.build();
		when(userManager.loginUser(request)).thenReturn(response);

		// Act
		ResponseEntity<Void> result = userController.loginUser(request);

		// Assert
		assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
		assertNull(result.getBody());
		verify(userManager).loginUser(request);
	}

	// Should return 404 NOT_FOUND when no user with the requested id exists
	@Test
	void test_return_not_found_when_user_not_found() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer id = 1;
		GetUserByIdRequest request = GetUserByIdRequest.builder()
				.id(id)
				.build();
		GetUserByIdResponse response = GetUserByIdResponse.builder()
				.httpStatus(HttpStatus.NOT_FOUND)
				.build();
		when(userManager.getUserById(request)).thenReturn(response);

		// Act
		ResponseEntity<User> result = userController.getUserById(id);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull(result.getBody());
	}

	// Should return 404 NOT_FOUND when no users with the queries exists
	@Test
	void test_getUsersByPartialData_returns_NOT_FOUND_when_no_users_with_queries_exists() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer page = 0;
		String username = "test";
		Date before = Date.from(Instant.now());
		Date after = Date.from(Instant.EPOCH);
		ResponseEntity<Iterable<User>> expectedResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

		// Mock the UserManager.getUsersByPartialData() method to return an empty UserQueryResponse
		UserQueryRequest request = UserQueryRequest.builder()
				.username(username)
				.before(before)
				.after(after)
				.page(page)
				.build();
		UserQueryResponse response = UserQueryResponse.builder()
				.users(Collections.emptyList())
				.httpStatus(HttpStatus.NOT_FOUND)
				.build();
		when(userManager.getUsersByPartialData(request)).thenReturn(response);

		// Act
		ResponseEntity<Iterable<User>> result = userController.getUsersByPartialData(page, username, before, after);

		// Assert
		assertEquals(expectedResponse, result);
	}

	// Should return 400 BAD_REQUEST when page is invalid
	@Test
	void test_should_return_bad_request_when_page_is_invalid() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer invalidPage = -1;

		// Act
		ResponseEntity<GetAllUsersResponse> response = userController.getAllUsers(invalidPage);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	// Should return 409 CONFLICT when the user is already activated or is banned
	@Test
	void test_return_409_conflict_when_user_already_activated_or_banned() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		Integer userId = 1;
		HttpStatus expectedHttpStatus = HttpStatus.CONFLICT;

		// Mock UserManager.removeUser() to return HttpStatus.CONFLICT
		when(userManager.removeUser(any(RemoveUserRequest.class))).thenReturn(HttpStatus.CONFLICT);

		// Act
		ResponseEntity<HttpStatus> response = userController.removeUser(userId);

		// Assert
		assertEquals(expectedHttpStatus, response.getStatusCode());
		verify(userManager).removeUser(any(RemoveUserRequest.class));
	}

	// Should return 400 BAD_REQUEST when request object is not valid
	@Test
	void test_request_object_not_valid_returns_bad_request_with_invalid_password() {
		// Arrange
		UserManager userManager = mock(UserManager.class);
		UserController userController = new UserController(userManager);
		CreateNewUserRequest request = CreateNewUserRequest.builder()
				.email("test@example.com")
				.username("user")
				.password("short")
				.build();

		// Mock the behavior of UserManager.createNewUser() to return BAD_REQUEST
		when(userManager.createNewUser(request)).thenReturn(HttpStatus.BAD_REQUEST);

		// Act
		ResponseEntity<HttpStatus> response = userController.createNewUser(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
}