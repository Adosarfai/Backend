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
import com.adosar.backend.business.service.JWTService;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import com.password4j.BadParametersException;
import com.password4j.Hash;
import com.password4j.Password;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class UserManagerImpl implements UserManager {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private final UserRepository userRepository;

	@Override
	public HttpStatus ActivateUser(ActivateUserRequest request) {
		try {
			assert request.getId() >= 1 : "'id' must be at least 1";

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
			assert request.getUsername().length() >= 3 : "'username' must have a length of at least 3";
			assert request.getPassword().length() >= 10 : "'password' must have a length of at least 10";

			// Hash password
			Hash hash = Password.hash(request.getPassword())
					.addRandomSalt(32)
					.withArgon2();

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
			// TODO: Send verification email

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
			assert request.getPage() >= 0 : "'page' must be at least 0";

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
			assert request.getId() >= 1 : "'id' must be at least 1";

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
			assert request.getEmail().length() >= 3 : "'email' must have a length of at least 3";
			assert request.getPassword().length() >= 10 : "'password' must have a length of at least 10";

			// Get user
			UserEntity userEntity = userRepository.getUserEntityByEmail(request.getEmail()).orElseThrow(() ->
					new NotFoundException(String.format("User with email %s was not found", request.getEmail()))
			);

			User user = UserConverter.convert(userEntity);

			// Invalid password
			if (!Password.check(request.getPassword(), user.getPassword()).withArgon2())
				throw new UnauthorizedException("Password hashes do not match");

			// Create jwt
			String jwt = JWTService.createJWT(user.getUserId());

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
	public HttpStatus RemoveUser(RemoveUserRequest request) {
		try {
			assert request.getId() >= 1 : "'id' must be at least 1";

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
			assert request.getPage() >= 0 : "'page' must be at least 0";

			List<UserEntity> userEntities = userRepository.getUserEntitiesByUsernameContainsAndCreationDateBeforeAndCreationDateAfter(request.getUsername(), request.getBefore(), request.getAfter(), PageRequest.of(request.getPage(), 10)).orElseThrow(() ->
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
}
