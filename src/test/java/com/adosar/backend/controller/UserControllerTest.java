package com.adosar.backend.controller;

import com.adosar.backend.business.GetAllUsersUseCase;
import com.adosar.backend.business.impl.GetAllUsersUseCaseImpl;
import com.adosar.backend.business.impl.GetUserByIdUseCaseImpl;
import com.adosar.backend.domain.User;
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
        ResponseEntity<Iterable<User>> responseEntity = userController.getAllUsers(1);
        
        // Assert
        assertThat(responseEntity.getBody()).isEmpty();
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
        ResponseEntity<Iterable<User>> responseEntity = userController.getAllUsers(-1);
        
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
        ResponseEntity<User> responseEntity = userController.getUserById(-1);

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
        ResponseEntity<User> responseEntity = userController.getUserById(1);

        // Assert
        assertThat(responseEntity.getBody()).isNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
