package com.adosar.backend.controller;

import com.adosar.backend.business.impl.*;
import com.adosar.backend.business.request.CreateNewUserRequest;
import com.adosar.backend.business.request.LoginUserRequest;
import com.adosar.backend.business.response.GetAllUsersResponse;
import com.adosar.backend.business.response.GetUserByIdResponse;
import com.adosar.backend.business.response.LoginUserResponse;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    /**
     * @verifies return empty list when no users are on the page
     * @see UserController#getAllUsers(Integer)
     */
    @Test
    public void getAllUsers_shouldReturnEmptyListWhenNoUsersAreOnThePage() throws Exception {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        Page<UserEntity> testUsers = new PageImpl<UserEntity>(List.of(new UserEntity()));
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
    }

    /**
     * @verifies return 400 BAD_REQUEST when page number is invalid
     * @see UserController#getAllUsers(Integer)
     */
    @Test
    public void getAllUsers_shouldReturn400BAD_REQUESTWhenPageNumberIsInvalid() throws Exception {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        UserController userController = UserController.builder()
                .getAllUsersUseCase(new GetAllUsersUseCaseImpl(userRepository))
                .build();

        // Act
        ResponseEntity<GetAllUsersResponse> responseEntity = userController.getAllUsers(-1);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /**
     * @verifies return 400 BAD_REQUEST when ID is invalid
     * @see UserController#getUserById(Integer)
     */
    @Test
    public void getUserById_shouldReturn400BAD_REQUESTWhenIDIsInvalid() throws Exception {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        UserController userController = UserController.builder()
                .getUserByIdUseCase(new GetUserByIdUseCaseImpl(userRepository))
                .build();

        // Act
        ResponseEntity<GetUserByIdResponse> responseEntity = userController.getUserById(-1);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /**
     * @verifies return 404 NOT_FOUND when no user with ID exists
     * @see UserController#getUserById(Integer)
     */
    @Test
    public void getUserById_shouldReturn404NOT_FOUNDWhenNoUserWithIDExists() throws Exception {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        Optional<UserEntity> testUser = Optional.of(new UserEntity());
        when(userRepository.findById(0)).thenReturn(testUser);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserController userController = UserController.builder()
                .getUserByIdUseCase(new GetUserByIdUseCaseImpl(userRepository))
                .build();

        // Act
        ResponseEntity<GetUserByIdResponse> responseEntity = userController.getUserById(1);

        // Assert
        assertThat(responseEntity.getBody().getUser()).isNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * @verifies return 400 BAD_REQUEST when request object is not valid
     * @see UserController#createNewUser(CreateNewUserRequest)
     */
    @Test
    public void createNewUser_shouldReturn400BAD_REQUESTWhenRequestObjectIsNotValid() throws Exception {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        UserController userController = UserController.builder()
                .createNewUserUseCase(new CreateNewUserUseCaseImpl(userRepository))
                .build();

        // Act
        ResponseEntity<HttpStatus> result = userController.createNewUser(CreateNewUserRequest.builder().build());

        // Assert        
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /**
     * @verifies return 401 UNAUTHORIZED when the login credentials are invalid
     * @see UserController#LoginUser(com.adosar.backend.business.request.LoginUserRequest)
     */
    @Test
    public void LoginUser_shouldReturn401UNAUTHORIZEDWhenTheLoginCredentialsAreInvalid() throws Exception {
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
        ResponseEntity<LoginUserResponse> resultWrongPassword = userController.LoginUser(LoginUserRequest.builder()
                .email("test@email.com")
                .password("testPassword")
                .build()
        );
        ResponseEntity<LoginUserResponse> resultWrongEmail = userController.LoginUser(LoginUserRequest.builder()
                .email("wrong@email.com")
                .password("Epic-Passw0rd!")
                .build()
        );

        // Assert
        assertThat(resultWrongPassword.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(resultWrongEmail.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    /**
     * @verifies return 404 NOT_FOUND when no user with the requested id exists
     * @see UserController#RemoveUser(Integer)
     */
    @Test
    public void RemoveUser_shouldReturn404NOT_FOUNDWhenNoUserWithTheRequestedIdExists() throws Exception {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        UserController userController = UserController.builder()
                .removeUserUseCase(new RemoveUserUseCaseImpl(userRepository))
                .build();

        // Act
        ResponseEntity<HttpStatus> result = userController.RemoveUser(0);

        // Assert        
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * @verifies return 409 CONFLICT when the user is already removed or is banned
     * @see UserController#RemoveUser(Integer)
     */
    @Test
    public void RemoveUser_shouldReturn409CONFLICTWhenTheUserIsAlreadyRemovedOrIsBanned() throws Exception {
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
        ResponseEntity<HttpStatus> resultRemoved = userController.RemoveUser(0);
        ResponseEntity<HttpStatus> resultBanned = userController.RemoveUser(1);

        // Assert        
        assertThat(resultRemoved.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(resultBanned.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    /**
     * @verifies return 404 NOT_FOUND when no user with the requested id exists
     * @see UserController#ActivateUser(Integer)
     */
    @Test
    public void ActivateUser_shouldReturn404NOT_FOUNDWhenNoUserWithTheRequestedIdExists() throws Exception {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);

        UserController userController = UserController.builder()
                .activateUserUseCase(new ActivateUserUseCaseImpl(userRepository))
                .build();

        // Act
        ResponseEntity<HttpStatus> result = userController.ActivateUser(0);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * @verifies return 409 CONFLICT when the user is already activated or is banned
     * @see UserController#ActivateUser(Integer)
     */
    @Test
    public void ActivateUser_shouldReturn409CONFLICTWhenTheUserIsAlreadyActivatedOrIsBanned() throws Exception {
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
        ResponseEntity<HttpStatus> resultUser = userController.ActivateUser(0);
        ResponseEntity<HttpStatus> resultAdmin = userController.ActivateUser(1);
        ResponseEntity<HttpStatus> resultBanned = userController.ActivateUser(2);

        // Assert
        assertThat(resultUser.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(resultAdmin.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(resultBanned.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
