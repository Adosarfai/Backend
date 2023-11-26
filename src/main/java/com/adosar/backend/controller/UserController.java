package com.adosar.backend.controller;

import com.adosar.backend.business.UserManager;
import com.adosar.backend.business.request.user.*;
import com.adosar.backend.business.response.user.GetAllUsersResponse;
import com.adosar.backend.business.response.user.GetUserByIdResponse;
import com.adosar.backend.business.response.user.LoginUserResponse;
import com.adosar.backend.business.response.user.UserQueryResponse;
import com.adosar.backend.domain.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"https://dev.adosar.io:5173", "https://adosar.io", "https://localhost:5137"})
@RequestMapping(path = "/user")
@AllArgsConstructor
@Builder
public class UserController {
	private final UserManager userManager;


	/**
	 * Gets 10 users per page
	 *
	 * @param page page number, starts at 0
	 * @return 10 users offset by the page number
	 * @should return empty list when no users are on the page
	 * @should return 400 BAD_REQUEST when page number is invalid
	 * @see GetAllUsersRequest
	 * @see GetAllUsersResponse
	 */
	@GetMapping(path = "/all/{page}")
	public @ResponseBody ResponseEntity<GetAllUsersResponse> getAllUsers(@PathVariable Integer page) {
		GetAllUsersRequest request = new GetAllUsersRequest(page);
		GetAllUsersResponse response = userManager.getAllUsers(request);
		return new ResponseEntity<>(response, response.getHttpStatus());
	}

	/**
	 * Gets a user object by ID
	 *
	 * @param id userId
	 * @return 200 OK with GetUserByIdResponse
	 * @should return 400 BAD_REQUEST when ID is invalid
	 * @should return 404 NOT_FOUND when no user with ID exists
	 * @see GetUserByIdRequest
	 * @see GetUserByIdResponse
	 */
	@GetMapping(path = "/{id}")
	public @ResponseBody ResponseEntity<User> getUserById(@PathVariable Integer id) {
		GetUserByIdRequest request = new GetUserByIdRequest(id);
		GetUserByIdResponse response = userManager.getUserById(request);
		return new ResponseEntity<>(response.getUser(), response.getHttpStatus());
	}

	/**
	 * Creates a new user
	 *
	 * @param request CreateNewUserRequest object
	 * @return 201 CREATED
	 * @should return 400 BAD_REQUEST when request object is not valid
	 * @see CreateNewUserRequest
	 * @see HttpStatus
	 */
	@PostMapping
	public @ResponseBody ResponseEntity<HttpStatus> createNewUser(@RequestBody @Valid CreateNewUserRequest request) {
		HttpStatus response = userManager.createNewUser(request);
		return new ResponseEntity<>(null, response);
	}

	/**
	 * Creates JWT for user auth
	 *
	 * @param request LoginUserRequest object
	 * @return 200 OK with LoginUserResponse object
	 * @should return 401 UNAUTHORIZED when the login credentials are invalid
	 * @see LoginUserRequest
	 * @see LoginUserResponse
	 */
	@PostMapping(path = "/login")
	public @ResponseBody ResponseEntity<Void> loginUser(@RequestBody @Valid LoginUserRequest request) {
		LoginUserResponse response = userManager.loginUser(request);
		if (response.getHttpStatus().is2xxSuccessful() && response.getJwt() != null) {
			ResponseCookie cookie = ResponseCookie.from("jwt", response.getJwt())
					.httpOnly(false)
					.secure(true)
					.path("/")
					.domain("dev.adosar.net")
					.maxAge(604800)
					.build();
			return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
		}
		return new ResponseEntity<>(null, response.getHttpStatus());
	}

	/**
	 * Deactivates user account
	 *
	 * @param id userId
	 * @return 200 OK
	 * @should return 404 NOT_FOUND when no user with the requested id exists
	 * @should return 409 CONFLICT when the user is already removed or is banned
	 * @see RemoveUserRequest
	 * @see HttpStatus
	 */
	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<HttpStatus> removeUser(@PathVariable Integer id) {
		RemoveUserRequest request = new RemoveUserRequest(id);
		HttpStatus response = userManager.RemoveUser(request);
		return new ResponseEntity<>(null, response);
	}


	/**
	 * Activates user account
	 *
	 * @param id userId
	 * @return 200 OK
	 * @should return 404 NOT_FOUND when no user with the requested id exists
	 * @should return 409 CONFLICT when the user is already activated or is banned
	 * @see ActivateUserRequest
	 * @see HttpStatus
	 */
	@PatchMapping("/{id}")
	public @ResponseBody ResponseEntity<HttpStatus> activateUser(@PathVariable Integer id) {
		ActivateUserRequest request = new ActivateUserRequest(id);
		HttpStatus response = userManager.ActivateUser(request);
		return new ResponseEntity<>(null, response);
	}

	/**
	 * Gets all users that match provided queries
	 *
	 * @param page     page-number
	 * @param username username
	 * @param before   before
	 * @param after    after
	 * @return 200 OK with Iterable<User>
	 * @should return 400 BAD_REQUEST when page is invalid
	 * @should return 404 NOT_FOUND when no users with the queries exists
	 * @see UserQueryRequest
	 * @see UserQueryResponse
	 */
	@GetMapping(path = "/query/{page}")
	public @ResponseBody ResponseEntity<Iterable<User>> getUsersByPartialData(@PathVariable Integer page, @RequestParam(value = "username", required = false) String username, @RequestParam(value = "before", required = false) Date before, @RequestParam(value = "after", required = false) Date after) {
		if (username == null || username.isEmpty()) username = "";
		if (before == null) before = Date.from(Instant.now());
		if (after == null) after = Date.from(Instant.EPOCH);
		UserQueryRequest request = UserQueryRequest.builder()
				.username(username)
				.before(before)
				.after(after)
				.page(page)
				.build();
		UserQueryResponse response = userManager.getUsersByPartialData(request);
		return new ResponseEntity<>(response.getUsers(), response.getHttpStatus());
	}
}
