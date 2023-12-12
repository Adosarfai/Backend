package com.adosar.backend.business.impl;

import com.adosar.backend.business.converter.UserConverter;
import com.adosar.backend.business.request.user.*;
import com.adosar.backend.business.response.user.GetAllUsersResponse;
import com.adosar.backend.business.response.user.GetUserByIdResponse;
import com.adosar.backend.business.response.user.LoginUserResponse;
import com.adosar.backend.business.response.user.UserQueryResponse;
import com.adosar.backend.business.service.JWTService;
import com.adosar.backend.business.service.PasswordService;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("UnusedShould")
class UserManagerImplTest {
	// loginUser method logs in a user with valid credentials
	@Test
	void can_login_user_with_valid_credentials() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		LoginUserRequest request = LoginUserRequest.builder()
				.email("test@example.com")
				.password("password123")
				.build();
		UserEntity userEntity = UserEntity.builder()
				.userId(1)
				.email(request.getEmail())
				.password(PasswordService.hashPassword(request.getPassword()).getResult())
				.build();
		when(userRepository.getUserEntityByEmail(request.getEmail())).thenReturn(Optional.of(userEntity));

		// Act
		LoginUserResponse response = userManager.loginUser(request);

		// Assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getHttpStatus());
		assertNotNull(response.getJwt());
	}

	// createNewUser method creates a new user with valid parameters
	@Test
	void create_new_user_successfully_with_valid_parameters() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		CreateNewUserRequest request = CreateNewUserRequest.builder()
				.email("test@example.com")
				.username("testuser")
				.password("password123")
				.build();

		// Mock UserRepository.saveAndFlush() to return a valid UserEntity
		UserEntity userEntity = UserEntity.builder()
				.userId(1)
				.email(request.getEmail())
				.username(request.getUsername())
				.password(request.getPassword())
				.privilege(Privilege.USER)
				.creationDate(Date.from(Instant.now()))
				.badges(Set.of())
				.build();
		when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(userEntity);

		// Act
		HttpStatus result = userManager.createNewUser(request);

		// Assert
		assertEquals(HttpStatus.CREATED, result);
		verify(userRepository).saveAndFlush(any(UserEntity.class));
	}

	// getUserById method retrieves a user by their ID
	@Test
	void can_retrieve_user_by_id_with_valid_input() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		GetUserByIdRequest request = GetUserByIdRequest.builder()
				.id(1)
				.build();
		UserEntity userEntity = UserEntity.builder()
				.userId(1)
				.username("testUser")
				.description("Test user")
				.privilege(Privilege.USER)
				.creationDate(Date.from(Instant.now()))
				.build();
		User expectedUser = UserConverter.convert(userEntity);
		when(userRepository.getUserEntityByUserId(request.getId())).thenReturn(Optional.of(userEntity));

		// Act
		GetUserByIdResponse response = userManager.getUserById(request);

		// Assert
		assertEquals(HttpStatus.OK, response.getHttpStatus());
		assertEquals(expectedUser, response.getUser());
	}

	// removeUser method removes a user with a USER or ADMIN privilege
	@Test
	void test_remove_user_removes_user_with_user_or_admin_privilege() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		RemoveUserRequest request = RemoveUserRequest.builder()
				.id(1)
				.build();
		UserEntity userEntity = UserEntity.builder()
				.userId(1)
				.privilege(Privilege.USER)
				.build();

		// Mock userRepository.getUserEntityByUserId() to return the userEntity
		when(userRepository.getUserEntityByUserId(request.getId())).thenReturn(Optional.of(userEntity));

		// Act
		HttpStatus result = userManager.removeUser(request);

		// Assert
		assertEquals(HttpStatus.OK, result);
		verify(userRepository).updatePrivilegeByUserId(userEntity.getUserId(), Privilege.REMOVED);
	}

	// getAllUsers method retrieves all users with valid parameters
	@Test
	void can_retrieve_all_users_successfully_with_valid_parameters() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		GetAllUsersRequest request = GetAllUsersRequest.builder()
				.page(0)
				.build();

		List<UserEntity> userEntities = new ArrayList<>();
		userEntities.add(UserEntity.builder()
				.userId(1)
				.username("User 1")
				.description("Description 1")
				.privilege(Privilege.USER)
				.creationDate(Date.from(Instant.now()))
				.build());
		userEntities.add(UserEntity.builder()
				.userId(2)
				.username("User 2")
				.description("Description 2")
				.privilege(Privilege.USER)
				.creationDate(Date.from(Instant.now()))
				.build());

		List<User> expectedUsers = userEntities.stream()
				.map(UserConverter::convert)
				.collect(Collectors.toList());

		when(userRepository.findAll(PageRequest.of(request.getPage(), 10))).thenReturn(new PageImpl<>(userEntities));

		// Act
		GetAllUsersResponse response = userManager.getAllUsers(request);

		// Assert
		assertEquals(expectedUsers, response.getUsers());
		assertEquals(HttpStatus.OK, response.getHttpStatus());
	}

	// loginUser method returns BAD_REQUEST when email is less than 3 characters
	@Test
	void test_loginUser_returns_BAD_REQUEST_when_email_is_less_than_3_characters() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		LoginUserRequest request = LoginUserRequest.builder()
				.email("ab")
				.password("password")
				.build();

		// Act
		LoginUserResponse response = userManager.loginUser(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
		assertNull(response.getJwt());
	}

	// createNewUser method returns BAD_REQUEST when password is less than 10 characters
	@Test
	void test_createNewUser_returns_BAD_REQUEST_when_password_is_less_than_10_characters() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		CreateNewUserRequest request = CreateNewUserRequest.builder()
				.email("test@example.com")
				.username("testuser")
				.password("123456789") // Password is less than 10 characters
				.build();

		// Act
		HttpStatus result = userManager.createNewUser(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, result);
	}

	// createNewUser method returns BAD_REQUEST when username is less than 3 characters
	@Test
	void test_createNewUser_returns_BAD_REQUEST_when_username_is_less_than_3_characters() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		CreateNewUserRequest request = CreateNewUserRequest.builder()
				.email("test@example.com")
				.username("aa")
				.password("password123")
				.build();

		// Act
		HttpStatus result = userManager.createNewUser(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, result);
	}

	// loginUser method returns BAD_REQUEST when password is less than 10 characters
	@Test
	void test_loginUser_returns_BAD_REQUEST_when_password_is_less_than_10_characters() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		LoginUserRequest request = LoginUserRequest.builder()
				.email("test@example.com")
				.password("123456789")
				.build();

		// Act
		LoginUserResponse response = userManager.loginUser(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
	}

	// removeUser method returns BAD_REQUEST when ID is less than 1
	@Test
	void test_remove_user_returns_bad_request_when_id_is_less_than_1() {
		// Arrange
		UserManagerImpl userManager = new UserManagerImpl(mock(UserRepository.class));
		RemoveUserRequest request = RemoveUserRequest.builder()
				.id(0)
				.build();

		// Act
		HttpStatus result = userManager.removeUser(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, result);
	}

	// getUserById method returns BAD_REQUEST when ID is less than 1
	@Test
	void test_getUserById_returns_BAD_REQUEST_when_ID_is_less_than_1() {
		// Arrange
		UserManagerImpl userManager = new UserManagerImpl(mock(UserRepository.class));
		GetUserByIdRequest request = GetUserByIdRequest.builder()
				.id(0)
				.build();

		// Act
		GetUserByIdResponse response = userManager.getUserById(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
		assertNull(response.getUser());
	}

	// activateUser method returns BAD_REQUEST when ID is less than 1
	@Test
	void test_activate_user_returns_bad_request_when_id_is_less_than_1() {
		// Arrange
		UserManagerImpl userManager = new UserManagerImpl(mock(UserRepository.class));
		ActivateUserRequest request = ActivateUserRequest.builder()
				.id(0)
				.build();

		// Act
		HttpStatus result = userManager.activateUser(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, result);
	}

	// loginUser method returns UNAUTHORIZED when password hashes do not match
	@Test
	void test_loginUser_returns_UNAUTHORIZED_when_password_hashes_do_not_match() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		LoginUserRequest request = LoginUserRequest.builder()
				.email("test@example.com")
				.password("password123")
				.build();
		UserEntity userEntity = UserEntity.builder()
				.email(request.getEmail())
				.password("incorrect_hashed_password")
				.build();
		when(userRepository.getUserEntityByEmail(request.getEmail())).thenReturn(Optional.of(userEntity));

		// Act
		LoginUserResponse response = userManager.loginUser(request);

		// Assert
		assertEquals(HttpStatus.UNAUTHORIZED, response.getHttpStatus());
		assertNull(response.getJwt());
	}

	// getUserById method returns NOT_FOUND when user with ID is not found
	@Test
	void test_getUserById_returns_NOT_FOUND_when_user_with_ID_is_not_found() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		GetUserByIdRequest request = GetUserByIdRequest.builder()
				.id(1)
				.build();

		// Mock the behavior of userRepository.getUserEntityByUserId() to return an empty Optional
		when(userRepository.getUserEntityByUserId(request.getId())).thenReturn(Optional.empty());

		// Act
		GetUserByIdResponse response = userManager.getUserById(request);

		// Assert
		assertNull(response.getUser());
		assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
	}

	// removeUser method returns NOT_FOUND when user with ID is not found
	@Test
	void test_remove_user_returns_not_found_when_user_with_id_not_found() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		RemoveUserRequest request = RemoveUserRequest.builder()
				.id(1)
				.build();

		// Mock the behavior of userRepository.getUserEntityByUserId() to return an empty Optional
		when(userRepository.getUserEntityByUserId(request.getId())).thenReturn(Optional.empty());

		// Act
		HttpStatus result = userManager.removeUser(request);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, result);
	}

	// activateUser method returns NOT_FOUND when user with ID is not found
	@Test
	void test_activate_user_returns_not_found_when_user_with_id_not_found() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		ActivateUserRequest request = ActivateUserRequest.builder()
				.id(1)
				.build();

		// Mock the behavior of userRepository.getUserEntityByUserId() to return an empty Optional
		when(userRepository.getUserEntityByUserId(request.getId())).thenReturn(Optional.empty());

		// Act
		HttpStatus result = userManager.activateUser(request);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, result);
	}

	// getUsersByPartialData method retrieves users with valid parameters
	@Test
	void can_retrieve_users_by_partial_data_with_valid_parameters() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		UserQueryRequest request = UserQueryRequest.builder()
				.username("test")
				.before(Date.from(Instant.now()))
				.after(Date.from(Instant.now()))
				.page(0)
				.build();
		List<UserEntity> userEntities = new ArrayList<>();
		userEntities.add(UserEntity.builder()
				.userId(1)
				.username("test1")
				.email("test1@example.com")
				.password("password1")
				.privilege(Privilege.USER)
				.creationDate(Date.from(Instant.now()))
				.build());
		userEntities.add(UserEntity.builder()
				.userId(2)
				.username("test2")
				.email("test2@example.com")
				.password("password2")
				.privilege(Privilege.USER)
				.creationDate(Date.from(Instant.now()))
				.build());
		when(userRepository.getUserEntitiesByUsernameContainsAndCreationDateBeforeAndCreationDateAfter(
				request.getUsername(), request.getBefore(), request.getAfter(), PageRequest.of(request.getPage(), 10)))
				.thenReturn(Optional.of(userEntities));
		List<User> expectedUsers = userEntities.stream().map(UserConverter::convert).collect(Collectors.toList());
		HttpStatus expectedHttpStatus = HttpStatus.OK;

		// Act
		UserQueryResponse response = userManager.getUsersByPartialData(request);

		// Assert
		assertEquals(expectedUsers, response.getUsers());
		assertEquals(expectedHttpStatus, response.getHttpStatus());
	}

	// getAllUsers method returns BAD_REQUEST when page is less than 0
	@Test
	void test_getAllUsers_returns_BAD_REQUEST_when_page_is_negative_10() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		GetAllUsersRequest request = GetAllUsersRequest.builder()
				.page(-10)
				.build();

		// Act
		GetAllUsersResponse response = userManager.getAllUsers(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
	}

	@Test
	void test_jwt_is_invalid() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		PatchUserWithPartialDataRequest request = new PatchUserWithPartialDataRequest();
		String jwt = "invalid_jwt";

		// Act
		HttpStatus result = userManager.patchUserWithPartialData(request, jwt);

		// Assert
		assertEquals(HttpStatus.UNAUTHORIZED, result);
	}

	@Test
	void test_user_does_not_exist() {
		// Arrange
		UserRepository userRepository = mock(UserRepository.class);
		UserManagerImpl userManager = new UserManagerImpl(userRepository);
		PatchUserWithPartialDataRequest request = PatchUserWithPartialDataRequest.builder()
				.username("newUsername")
				.email("newEmail@example.com")
				.password("newPassword123")
				.profilePicture("newProfilePicture")
				.build();
		String jwt = JWTService.createJWT(1);

		// Mock JWTService.verifyJWT() to return a valid DecodedJWT
		DecodedJWT decodedJWT = mock(DecodedJWT.class);

		// Mock DecodedJWT.getClaim() to return the userId
		Claim claim = mock(Claim.class);
		when(decodedJWT.getClaim("userId")).thenReturn(claim);
		when(claim.as(Integer.class)).thenReturn(1);

		// Mock userRepository.getUserEntityByUserId() to return an empty Optional
		when(userRepository.getUserEntityByUserId(1)).thenReturn(Optional.empty());

		// Act
		HttpStatus result = userManager.patchUserWithPartialData(request, jwt);

		// Assert
		assertEquals(HttpStatus.UNAUTHORIZED, result);
	}
}