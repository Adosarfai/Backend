package com.adosar.backend.controller;

import com.adosar.backend.business.impl.*;
import com.adosar.backend.business.request.CreateNewUserRequest;
import com.adosar.backend.business.request.LoginUserRequest;
import com.adosar.backend.business.response.GetAllUsersResponse;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.javapoet.ClassName;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());

	/**
	 * @verifies return empty list when no users are on the page
	 * @see UserController#getAllUsers(Integer)
	 */
	@Test
	public void getAllUsers_shouldReturnEmptyListWhenNoUsersAreOnThePage() {
		try {
			// Arrange
			UserRepository userRepository = mock(UserRepository.class);
			Page<UserEntity> testUsers = new PageImpl<>(List.of(new UserEntity()));
			when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(testUsers);
			when(userRepository.findAll(PageRequest.of(1, 10))).thenReturn(Page.empty());

			UserController userController = UserController.builder()
					.getAllUsersUseCase(new GetAllUsersUseCaseImpl(userRepository))
					.build();

			// Act
			ResponseEntity<GetAllUsersResponse> responseEntity = userController.getAllUsers(1);

			// Assert
			assertThat(responseEntity.getBody().getUsers()).isEmpty();
			assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
		}
	}

	/**
	 * @verifies return 400 BAD_REQUEST when page number is invalid
	 * @see UserController#getAllUsers(Integer)
	 */
	@Test
	public void getAllUsers_shouldReturn400BAD_REQUESTWhenPageNumberIsInvalid() {
		try {
			// Arrange
			UserRepository userRepository = mock(UserRepository.class);

			UserController userController = UserController.builder()
					.getAllUsersUseCase(new GetAllUsersUseCaseImpl(userRepository))
					.build();

			// Act
			ResponseEntity<GetAllUsersResponse> responseEntity = userController.getAllUsers(-1);

			// Assert
			assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
		}
	}

	/**
	 * @verifies return 400 BAD_REQUEST when ID is invalid
	 * @see UserController#getUserById(Integer)
	 */
	@Test
	public void getUserById_shouldReturn400BAD_REQUESTWhenIDIsInvalid() {
		try {
			// Arrange
			UserRepository userRepository = mock(UserRepository.class);

			UserController userController = UserController.builder()
					.getUserByIdUseCase(new GetUserByIdUseCaseImpl(userRepository))
					.build();

			// Act
			ResponseEntity<User> responseEntity = userController.getUserById(-1);

			// Assert
			assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
		}
	}

	/**
	 * @verifies return 404 NOT_FOUND when no user with ID exists
	 * @see UserController#getUserById(Integer)
	 */
	@Test
	public void getUserById_shouldReturn404NOT_FOUNDWhenNoUserWithIDExists() {
		try {
			// Arrange
			UserRepository userRepository = mock(UserRepository.class);
			Optional<UserEntity> testUser = Optional.of(new UserEntity());
			when(userRepository.findById(0)).thenReturn(testUser);
			when(userRepository.findById(1)).thenReturn(Optional.empty());

			UserController userController = UserController.builder()
					.getUserByIdUseCase(new GetUserByIdUseCaseImpl(userRepository))
					.build();

			// Act
			ResponseEntity<User> responseEntity = userController.getUserById(1);

			// Assert
			assertThat(responseEntity.getBody()).isNull();
			assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
		}
	}

	/**
	 * @verifies return 400 BAD_REQUEST when request object is not valid
	 * @see UserController#createNewUser(CreateNewUserRequest)
	 */
	@Test
	public void createNewUser_shouldReturn400BAD_REQUESTWhenRequestObjectIsNotValid() {
		try {
			// Arrange
			UserRepository userRepository = mock(UserRepository.class);

			UserController userController = UserController.builder()
					.createNewUserUseCase(new CreateNewUserUseCaseImpl(userRepository))
					.build();

			// Act
			ResponseEntity<HttpStatus> result = userController.createNewUser(CreateNewUserRequest.builder().build());

			// Assert        
			assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
		}
	}

	/**
	 * @verifies return 401 UNAUTHORIZED when the login credentials are invalid
	 * @see UserController#loginUser(com.adosar.backend.business.request.LoginUserRequest)
	 */
	@Test
	public void loginUser_shouldReturn401UNAUTHORIZEDWhenTheLoginCredentialsAreInvalid() {
		try {
			// Arrange
			UserRepository userRepository = mock(UserRepository.class);
			when(userRepository.getUserEntityByEmail("wrong@email.com")).thenReturn(null);
			when(userRepository.getUserEntityByEmail("test@email.com")).thenReturn(UserEntity.builder()
					.userId(0)
					.email("test@email.com")
					.password("$argon2id$v=19$m=4096,t=30,p=4$Nohz9k/BrmmV/VkgLkKiZPYkOcpRXJN7pVpSB6pgHYU$wy48WkPXVrJAV5eKIQn8SHuUQ+1ihlXkM4ximB+7klfzOQTfCAUnIbREHtWkR4zlN35iwwn7odSbzYWnqvlR+Q")
					.build()
			);

			UserController userController = UserController.builder()
					.loginUserUseCase(new LoginUserUseCaseImpl(userRepository))
					.build();

			// Act
			ResponseEntity<Void> resultWrongPassword = userController.loginUser(LoginUserRequest.builder()
					.email("test@email.com")
					.password("testPassword")
					.build()
			);
			ResponseEntity<Void> resultWrongEmail = userController.loginUser(LoginUserRequest.builder()
					.email("wrong@email.com")
					.password("Epic-Passw0rd!")
					.build()
			);

			// Assert
			assertThat(resultWrongPassword.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
			assertThat(resultWrongEmail.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
		}
	}

	/**
	 * @verifies return 404 NOT_FOUND when no user with the requested id exists
	 * @see UserController#removeUser(Integer)
	 */
	@Test
	public void removeUser_shouldReturn404NOT_FOUNDWhenNoUserWithTheRequestedIdExists() {
		try {
			// Arrange
			UserRepository userRepository = mock(UserRepository.class);

			UserController userController = UserController.builder()
					.removeUserUseCase(new RemoveUserUseCaseImpl(userRepository))
					.build();

			// Act
			ResponseEntity<HttpStatus> result = userController.removeUser(0);

			// Assert        
			assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
		}
	}

	/**
	 * @verifies return 409 CONFLICT when the user is already removed or is banned
	 * @see UserController#removeUser(Integer)
	 */
	@Test
	public void removeUser_shouldReturn409CONFLICTWhenTheUserIsAlreadyRemovedOrIsBanned() {
		try {
			// Arrange
			UserRepository userRepository = mock(UserRepository.class);
			when(userRepository.getUserEntityByUserId(0)).thenReturn(UserEntity.builder()
					.userId(0)
					.privilege(Privilege.REMOVED)
					.build()
			);
			when(userRepository.getUserEntityByUserId(1)).thenReturn(UserEntity.builder()
					.userId(1)
					.privilege(Privilege.BANNED)
					.build()
			);

			UserController userController = UserController.builder()
					.removeUserUseCase(new RemoveUserUseCaseImpl(userRepository))
					.build();

			// Act
			ResponseEntity<HttpStatus> resultRemoved = userController.removeUser(0);
			ResponseEntity<HttpStatus> resultBanned = userController.removeUser(1);

			// Assert        
			assertThat(resultRemoved.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
			assertThat(resultBanned.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
		}
	}

	/**
	 * @verifies return 404 NOT_FOUND when no user with the requested id exists
	 * @see UserController#activateUser(Integer)
	 */
	@Test
	public void activateUser_shouldReturn404NOT_FOUNDWhenNoUserWithTheRequestedIdExists() {
		try {
			// Arrange
			UserRepository userRepository = mock(UserRepository.class);

			UserController userController = UserController.builder()
					.activateUserUseCase(new ActivateUserUseCaseImpl(userRepository))
					.build();

			// Act
			ResponseEntity<HttpStatus> result = userController.activateUser(0);

			// Assert
			assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
		}
	}

	/**
	 * @verifies return 409 CONFLICT when the user is already activated or is banned
	 * @see UserController#activateUser(Integer)
	 */
	@Test
	public void activateUser_shouldReturn409CONFLICTWhenTheUserIsAlreadyActivatedOrIsBanned() {
		try {
			// Arrange
			UserRepository userRepository = mock(UserRepository.class);
			when(userRepository.getUserEntityByUserId(0)).thenReturn(UserEntity.builder()
					.userId(0)
					.privilege(Privilege.USER)
					.build()
			);
			when(userRepository.getUserEntityByUserId(1)).thenReturn(UserEntity.builder()
					.userId(1)
					.privilege(Privilege.ADMIN)
					.build()
			);
			when(userRepository.getUserEntityByUserId(2)).thenReturn(UserEntity.builder()
					.userId(2)
					.privilege(Privilege.BANNED)
					.build()
			);

			UserController userController = UserController.builder()
					.activateUserUseCase(new ActivateUserUseCaseImpl(userRepository))
					.build();

			// Act
			ResponseEntity<HttpStatus> resultUser = userController.activateUser(0);
			ResponseEntity<HttpStatus> resultAdmin = userController.activateUser(1);
			ResponseEntity<HttpStatus> resultBanned = userController.activateUser(2);

			// Assert
			assertThat(resultUser.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
			assertThat(resultAdmin.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
			assertThat(resultBanned.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
		}
	}
}
