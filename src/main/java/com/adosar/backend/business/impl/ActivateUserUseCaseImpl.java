package com.adosar.backend.business.impl;

import com.adosar.backend.business.ActivateUserUseCase;
import com.adosar.backend.business.exception.AlreadyModifiedException;
import com.adosar.backend.business.request.ActivateUserRequest;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class ActivateUserUseCaseImpl implements ActivateUserUseCase {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private UserRepository userRepository;

	@Override
	public HttpStatus ActivateUser(final ActivateUserRequest request) {
		try {

			UserEntity userEntity = userRepository.getUserEntityByUserId(request.getId());

			// No user with ID
			if (userEntity == null)
				throw new InvalidParameterException(String.format("Could not find user with id %s", request.getId()));
			User user = UserConverter.convert(userEntity);

			// Privilege is BANNED
			if (user.getPrivilege() == Privilege.BANNED) throw new AlreadyModifiedException("User is banned");

			// Privilege is already USER/ADMIN
			if (user.getPrivilege() == Privilege.USER
					    || user.getPrivilege() == Privilege.ADMIN)
				throw new AlreadyModifiedException("User is already activated");

			// Update user
			userRepository.updatePrivilegeByUserId(user.getUserId(), Privilege.USER);

			return HttpStatus.OK;
		} catch (InvalidParameterException invalidParameterException) {
			LOGGER.log(Level.FINE, invalidParameterException.toString(), invalidParameterException);
			return HttpStatus.NOT_FOUND;
		} catch (AlreadyModifiedException alreadyModifiedException) {
			LOGGER.log(Level.FINE, alreadyModifiedException.toString(), alreadyModifiedException);
			return HttpStatus.CONFLICT;
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
