package com.adosar.backend.controller;

import com.adosar.backend.business.GetAllUsersUseCase;
import com.adosar.backend.business.GetUserByIdUseCase;
import com.adosar.backend.domain.User;
import com.adosar.backend.domain.request.GetAllUsersRequest;
import com.adosar.backend.domain.request.GetUserByIdRequest;
import com.adosar.backend.domain.response.GetAllUsersResponse;
import com.adosar.backend.domain.response.GetUserByIdResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
@Builder
public class UserController {
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    

    /**
     * Gets 10 users per page
     * 
     * @param page page number, starts at 0
     * @return 10 users offset by the page number
     * 
     * @should return empty list when no users are on the page
     * @should return 400 BAD_REQUEST when page number is invalid
     * @should return 500 INTERNAL_SERVER_ERROR when something unexpected goes wrong
     */
    @GetMapping(path = "/all/{page}")
    public @ResponseBody ResponseEntity<Iterable<User>> getAllUsers(@PathVariable Integer page) {
        GetAllUsersRequest request = new GetAllUsersRequest(page);
        GetAllUsersResponse response = getAllUsersUseCase.getAllUsers(request);
        return new ResponseEntity<>(response.getUsers(), response.getHttpStatus());
    }

    /**
     * Gets a user object by ID
     * 
     * @param id id of the user
     * 
     * @return user object with provided ID
     * 
     * @should return 400 BAD_REQUEST when ID is invalid
     * @should return 404 NOT_FOUND when no user with ID exists
     * @should return 500 INTERNAL_SERVER_ERROR when something unexpected goes wrong
     */
    @GetMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<User> getUserById(@PathVariable Integer id) {
        GetUserByIdRequest request = new GetUserByIdRequest(id);
        GetUserByIdResponse response = getUserByIdUseCase.getUserById(request);
        return new ResponseEntity<>(response.getUser(), response.getHttpStatus());
    }
}
