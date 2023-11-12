package com.adosar.backend.business.impl;

import com.adosar.backend.business.GetUserByIdUseCase;
import com.adosar.backend.business.exception.BadRequestException;
import com.adosar.backend.business.exception.NotFoundException;
import com.adosar.backend.business.request.GetUserByIdRequest;
import com.adosar.backend.business.response.GetUserByIdResponse;
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
public class GetUserByIdUseCaseImpl implements GetUserByIdUseCase {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private UserRepository userRepository;

	@Override
	public GetUserByIdResponse getUserById(final GetUserByIdRequest request) {
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
}
