package com.adosar.backend.business.impl;

import com.adosar.backend.business.UserManager;
import com.adosar.backend.business.converter.UserConverter;
import com.adosar.backend.business.exception.ConflictException;
import com.adosar.backend.business.exception.NotFoundException;
import com.adosar.backend.business.exception.UnauthorizedException;
import com.adosar.backend.business.request.user.*;
import com.adosar.backend.business.response.user.GetAllUsersResponse;
import com.adosar.backend.business.response.user.GetUserByIdResponse;
import com.adosar.backend.business.response.user.LoginUserResponse;
import com.adosar.backend.business.response.user.UserQueryResponse;
import com.adosar.backend.business.service.AuthorizationService;
import com.adosar.backend.business.service.CdnService;
import com.adosar.backend.business.service.JWTService;
import com.adosar.backend.business.service.PasswordService;
import com.adosar.backend.domain.Authorization;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.password4j.BadParametersException;
import com.password4j.Hash;
import com.password4j.Password;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class UserManagerImpl implements UserManager {
	private static final Logger LOGGER = Logger.getLogger(UserManagerImpl.class.getName());
	private final UserRepository userRepository;

	@Override
	public HttpStatus activateUser(ActivateUserRequest request) {
		try {
			if (request.getId() < 1) throw new AssertionError(String.format("id %s must at least be 0", request.getId()));

			// Get user
			UserEntity userEntity = userRepository.getUserEntityByUserId(request.getId()).orElseThrow(() ->
					new NotFoundException(String.format("Could not find user with id %s", request.getId()))
			);

			User user = UserConverter.convert(userEntity);

			// Privilege is BANNED
			if (user.getPrivilege() == Privilege.BANNED) throw new ConflictException("User is banned");

			// Privilege is already USER/ADMIN
			if (user.getPrivilege() == Privilege.USER
					|| user.getPrivilege() == Privilege.ADMIN)
				throw new NotFoundException("User is already activated");

			// Update user
			userRepository.updatePrivilegeByUserId(user.getUserId(), Privilege.USER);

			return HttpStatus.OK;
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return HttpStatus.BAD_REQUEST;
		} catch (NotFoundException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return HttpStatus.NOT_FOUND;
		} catch (ConflictException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return HttpStatus.CONFLICT;
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public HttpStatus createNewUser(CreateNewUserRequest request) {
		try {
			if (request.getUsername().length() < 3)
				throw new AssertionError("'username' must have a length of at least 3");
			if (request.getPassword().length() < 10)
				throw new AssertionError("'password' must have a length of at least 10");

			// Hash password
			Hash hash = PasswordService.hashPassword(request.getPassword());

			// Create new user
			UserEntity newUser = UserEntity.builder()
					.password(hash.getResult())
					.email(request.getEmail())
					.privilege(Privilege.USER)
					.username(request.getUsername())
					.creationDate(Date.from(Instant.now()))
					.badges(Set.of())
					.build();

			userRepository.saveAndFlush(newUser);

			return HttpStatus.CREATED;
		} catch (BadParametersException | InvalidParameterException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return HttpStatus.BAD_REQUEST;
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public GetAllUsersResponse getAllUsers(GetAllUsersRequest request) {
		try {
			if (request.getPage() < 0) throw new AssertionError("'page' must be at least 0");

			// Get users
			List<UserEntity> result = userRepository.findAll(PageRequest.of(request.getPage(), 10)).toList();
			List<User> users = result.stream().map(UserConverter::convert).toList();

			return new GetAllUsersResponse(users, HttpStatus.OK);
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetAllUsersResponse(null, HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetAllUsersResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public GetUserByIdResponse getUserById(GetUserByIdRequest request) {
		try {
			if (request.getId() < 1) throw new AssertionError(String.format("id %s must at least be 0", request.getId()));

			// Get user
			UserEntity result = userRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException(String.format("User with ID %s was not found", request.getId())));
			User user = UserConverter.convert(result);

			return new GetUserByIdResponse(user, HttpStatus.OK);

		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetUserByIdResponse(null, HttpStatus.BAD_REQUEST);
		} catch (NotFoundException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetUserByIdResponse(null, HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetUserByIdResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public LoginUserResponse loginUser(LoginUserRequest request) {
		try {
			if (request.getEmail().length() < 3) throw new AssertionError("'email' must have a length of at least 3");
			if (request.getPassword().length() < 10)
				throw new AssertionError("'password' must have a length of at least 10");

			// Get user
			UserEntity userEntity = userRepository.getUserEntityByEmail(request.getEmail()).orElseThrow(() ->
					new NotFoundException(String.format("User with email %s was not found", request.getEmail()))
			);

			User user = UserConverter.convert(userEntity);

			// Invalid password
			if (!Password.check(request.getPassword(), user.getPassword()).withArgon2())
				throw new UnauthorizedException("Password hashes do not match");

			// Create jwt
			String jwt = JWTService.createJWT(user.getUserId(), user.getPrivilege());

			return new LoginUserResponse(jwt, HttpStatus.OK);

		} catch (UnauthorizedException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new LoginUserResponse(null, HttpStatus.UNAUTHORIZED);
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new LoginUserResponse(null, HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new LoginUserResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public HttpStatus removeUser(RemoveUserRequest request) {
		try {
			if (request.getId() < 1) throw new AssertionError(String.format("id %s must at least be 0", request.getId()));

			// Get user
			UserEntity userEntity = userRepository.getUserEntityByUserId(request.getId()).orElseThrow(() ->
					new NotFoundException(String.format("Could not find user with id %s", request.getId()))
			);
			User user = UserConverter.convert(userEntity);

			// User is removed
			if (user.getPrivilege() == Privilege.REMOVED) throw new ConflictException("User is already deleted");

			// User is banned
			if (user.getPrivilege() == Privilege.BANNED) throw new ConflictException("User is banned");

			// Update user
			userRepository.updatePrivilegeByUserId(user.getUserId(), Privilege.REMOVED);

			return HttpStatus.OK;
		} catch (NotFoundException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return HttpStatus.NOT_FOUND;
		} catch (ConflictException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return HttpStatus.CONFLICT;
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return HttpStatus.BAD_REQUEST;
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public UserQueryResponse getUsersByPartialData(UserQueryRequest request) {
		try {
			if (request.getPage() < 0) throw new AssertionError(String.format("page %s must at least be 0", request.getPage()));

			List<UserEntity> userEntities = userRepository.getUserEntitiesByUsernameContainsAndCreationDateBeforeAndCreationDateAfterAndPrivilegeInAllIgnoreCaseOrderByUserIdAsc(request.getUsername(), request.getBefore(), request.getAfter(), List.of(Privilege.USER, Privilege.ADMIN), PageRequest.of(request.getPage(), 10)).orElseThrow(() ->
					new NotFoundException(String.format("Could not find any users where username contains %s", request.getUsername()))
			);
			List<User> users = userEntities.stream().map(UserConverter::convert).toList();

			return new UserQueryResponse(users, HttpStatus.OK);
		} catch (NotFoundException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new UserQueryResponse(null, HttpStatus.NOT_FOUND);
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new UserQueryResponse(null, HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new UserQueryResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public HttpStatus patchUserWithPartialData(PatchUserWithPartialDataRequest request, String jwt) {
		try {
			// Check if user is authorized
			DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
			if (decodedJWT == null) throw new UnauthorizedException("Unable to decode JWT");
			Integer userId = decodedJWT.getClaim("userId").as(Integer.class);

			// Check if user exists
			UserEntity userEntity = userRepository.getUserEntityByUserId(userId).orElseThrow(() ->
					new UnauthorizedException(String.format("User with ID %s was not found", userId))
			);
			User user = UserConverter.convert(userEntity);

			// Update user fields
			if (request.getUsername() != null && !request.getUsername().isBlank()) {
				user.setUsername(request.getUsername());
			}
			if (request.getEmail() != null && !request.getEmail().isBlank()) {
				user.setEmail(request.getEmail());
			}
			if (request.getPassword() != null && !request.getPassword().isBlank()) {
				user.setPassword(PasswordService.hashPassword(request.getPassword()).getResult());
			}
			if (request.getProfilePicture() != null && !request.getProfilePicture().isBlank()) {
				// Convert base64 to png file
				String pfp = request.getProfilePicture();

				if (request.getProfilePicture().startsWith("data:")) {
					pfp = pfp.split(",")[1];
				}
				byte[] decodedFile = Base64.getMimeDecoder().decode(pfp);
				CdnService.saveFile(decodedFile, "\\user", String.format("%s.png", userId));
			}

			// Save changes
			userRepository.saveAndFlush(UserConverter.convert(user));

			return HttpStatus.ACCEPTED;
		} catch (JWTVerificationException | UnauthorizedException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return HttpStatus.UNAUTHORIZED;
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public HttpStatus toggleUserBan(String jwt, Integer id) {
		try {
			Authorization authorization = AuthorizationService.isAuthorized(jwt, Privilege.ADMIN, userRepository);
			if (Boolean.FALSE.equals(authorization.isAuthorized)) {
				throw new UnauthorizedException("Unauthorized");
			}

			Optional<UserEntity> userEntity = userRepository.getUserEntityByUserId(id);

			if (userEntity.isEmpty()) {
				throw new NotFoundException(String.format("User with id %s could not be found", id));
			}

			userEntity.get().setPrivilege(userEntity.get().getPrivilege() == Privilege.BANNED ? Privilege.USER : Privilege.BANNED);

			userRepository.saveAndFlush(userEntity.get());

			return HttpStatus.ACCEPTED;
		} catch (UnauthorizedException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return HttpStatus.UNAUTHORIZED;
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
