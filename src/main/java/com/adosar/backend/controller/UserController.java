package com.adosar.backend.controller;

import com.adosar.backend.business.CreateNewUserUseCase;
import com.adosar.backend.business.GetAllUsersUseCase;
import com.adosar.backend.business.GetUserByIdUseCase;
import com.adosar.backend.business.LoginUserUseCase;
import com.adosar.backend.controller.request.CreateNewUserRequest;
import com.adosar.backend.controller.request.GetAllUsersRequest;
import com.adosar.backend.controller.request.GetUserByIdRequest;
import com.adosar.backend.controller.request.LoginUserRequest;
import com.adosar.backend.controller.response.GetAllUsersResponse;
import com.adosar.backend.controller.response.GetUserByIdResponse;
import com.adosar.backend.controller.response.LoginUserResponse;
import com.adosar.backend.domain.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
@Builder
public class UserController {
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final CreateNewUserUseCase createNewUserUseCase;
    private final LoginUserUseCase loginUserUseCase;


    /**
     * Gets 10 users per page
     *
     * @param page page number, starts at 0
     * @return 10 users offset by the page number
     * @should return empty list when no users are on the page
     * @should return 400 BAD_REQUEST when page number is invalid
     * @should return 500 INTERNAL_SERVER_ERROR when something unexpected goes wrong
     */
    @GetMapping(path = "/all/{page}")
    public @ResponseBody ResponseEntity<GetAllUsersResponse> getAllUsers(@PathVariable Integer page) {
        GetAllUsersRequest request = new GetAllUsersRequest(page);
        GetAllUsersResponse response = getAllUsersUseCase.getAllUsers(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    /**
     * Gets a user object by ID
     *
     * @param id id of the user
     * @return user object with provided ID
     * @should return 400 BAD_REQUEST when ID is invalid
     * @should return 404 NOT_FOUND when no user with ID exists
     * @should return 500 INTERNAL_SERVER_ERROR when something unexpected goes wrong
     */
    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<GetUserByIdResponse> getUserById(@PathVariable Integer id) {
        GetUserByIdRequest request = new GetUserByIdRequest(id);
        GetUserByIdResponse response = getUserByIdUseCase.getUserById(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    /**
     * @param request CreateNewUserRequest object
     * @return 201 CREATED
     * @should return 400 BAD_REQUEST when request object is not valid
     * @should return 500 INTERNAL_SERVER_ERROR when something unexpected goes wrong
     * @see CreateNewUserRequest
     */
    @PostMapping
    public @ResponseBody ResponseEntity<HttpStatus> createNewUser(@RequestBody @Valid CreateNewUserRequest request) {
        HttpStatus response = createNewUserUseCase.createNewUser(request);
        return new ResponseEntity<>(response, response);
    }

    @PostMapping(path = "/login")
    public @ResponseBody ResponseEntity<LoginUserResponse> LoginUser(@RequestBody @Valid LoginUserRequest request) {
        LoginUserResponse response = loginUserUseCase.loginUser(request);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
