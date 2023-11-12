package com.adosar.backend.business.impl;

import com.adosar.backend.business.RemoveUserUseCase;
import com.adosar.backend.business.exception.ConflictException;
import com.adosar.backend.business.exception.NotFoundException;
import com.adosar.backend.business.request.RemoveUserRequest;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class RemoveUserUseCaseImpl implements RemoveUserUseCase {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private UserRepository userRepository;

	@Override
	public HttpStatus RemoveUser(final RemoveUserRequest request) {
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
