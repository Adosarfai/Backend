package com.adosar.backend.business.impl;

import com.adosar.backend.business.UserManager;
import com.adosar.backend.business.converter.UserConverter;
import com.adosar.backend.business.exception.BadRequestException;
import com.adosar.backend.business.exception.ConflictException;
import com.adosar.backend.business.exception.NotFoundException;
import com.adosar.backend.business.exception.UnauthorizedException;
import com.adosar.backend.business.request.user.*;
import com.adosar.backend.business.response.user.GetAllUsersResponse;
import com.adosar.backend.business.response.user.GetUserByIdResponse;
import com.adosar.backend.business.response.user.LoginUserResponse;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class UserManagerImpl implements UserManager {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private UserRepository userRepository;

	@Override
	public HttpStatus ActivateUser(ActivateUserRequest request) {
		try {
			// Get user
			UserEntity userEntity = userRepository.getUserEntityByUserId(request.getId());
			if (userEntity == null)
				throw new NotFoundException(String.format("Could not find user with id %s", request.getId()));
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
		} catch (NotFoundException notFoundException) {
			LOGGER.log(Level.FINE, notFoundException.toString(), notFoundException);
			return HttpStatus.NOT_FOUND;
		} catch (ConflictException conflictException) {
			LOGGER.log(Level.FINE, conflictException.toString(), conflictException);
			return HttpStatus.CONFLICT;
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public HttpStatus createNewUser(CreateNewUserRequest request) {
		try {
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
					.build();

			userRepository.saveAndFlush(newUser);
			// TODO: Send verification email

			return HttpStatus.CREATED;
		} catch (BadParametersException | InvalidParameterException badParametersException) {
			LOGGER.log(Level.FINE, badParametersException.toString(), badParametersException);
			return HttpStatus.BAD_REQUEST;
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

	@Override
	public GetAllUsersResponse getAllUsers(GetAllUsersRequest request) {
		try {
			// Check is page is valid
			if (request.getPage() < 0) throw new BadRequestException(request.getPage().toString());

			// Get users
			List<UserEntity> result = userRepository.findAll(PageRequest.of(request.getPage(), 10)).toList();
			List<User> users = result.stream().map(UserConverter::convert).toList();

			return new GetAllUsersResponse(users, HttpStatus.OK);
		} catch (BadRequestException badRequestException) {
			LOGGER.log(Level.FINE, badRequestException.toString(), badRequestException);
			return new GetAllUsersResponse(null, HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetAllUsersResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public GetUserByIdResponse getUserById(GetUserByIdRequest request) {
		try {
			// Check is id is valid
			if (request.getId() < 0) throw new BadRequestException(request.getId().toString());

			// Get user
			UserEntity result = userRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException(String.format("User with ID %s was not found", request.getId())));
			User user = UserConverter.convert(result);

			return new GetUserByIdResponse(user, HttpStatus.OK);

		} catch (BadRequestException badRequestException) {
			LOGGER.log(Level.FINE, badRequestException.toString(), badRequestException);
			return new GetUserByIdResponse(null, HttpStatus.BAD_REQUEST);
		} catch (NotFoundException notFoundException) {
			LOGGER.log(Level.FINE, notFoundException.toString(), notFoundException);
			return new GetUserByIdResponse(null, HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetUserByIdResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public LoginUserResponse loginUser(LoginUserRequest request) {
		try {
			// Get user
			UserEntity userEntity = userRepository.getUserEntityByEmail(request.getEmail());

			// Invalid email
			if (userEntity == null)
				throw new UnauthorizedException(String.format("User with email %s was not found", request.getEmail()));

			User user = UserConverter.convert(userEntity);

			// Invalid password
			if (!Password.check(request.getPassword(), user.getPassword()).withArgon2())
				throw new UnauthorizedException("Password hashes do not match");

			// Create jwt
			String jwt = JWTService.createJWT(user.getUserId());

			return new LoginUserResponse(jwt, HttpStatus.OK);

		} catch (UnauthorizedException unauthorizedException) {
			LOGGER.log(Level.FINE, unauthorizedException.toString(), unauthorizedException);
			return new LoginUserResponse(null, HttpStatus.UNAUTHORIZED);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new LoginUserResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public HttpStatus RemoveUser(RemoveUserRequest request) {
		try {
			// Get user
			UserEntity userEntity = userRepository.getUserEntityByUserId(request.getId());

			// No user with ID
			if (userEntity == null)
				throw new NotFoundException(String.format("Could not find user with id %s", request.getId()));
			User user = UserConverter.convert(userEntity);

			// User is banned
			if (user.getPrivilege() == Privilege.BANNED) throw new ConflictException("User is banned");

			// User is removed
			if (user.getPrivilege() == Privilege.REMOVED) throw new ConflictException("User is already deleted");


			// Update user
			userRepository.updatePrivilegeByUserId(user.getUserId(), Privilege.REMOVED);

			return HttpStatus.OK;
		} catch (NotFoundException notFoundException) {
			LOGGER.log(Level.FINE, notFoundException.toString(), notFoundException);
			return HttpStatus.NOT_FOUND;
		} catch (ConflictException conflictException) {
			LOGGER.log(Level.FINE, conflictException.toString(), conflictException);
			return HttpStatus.CONFLICT;
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
