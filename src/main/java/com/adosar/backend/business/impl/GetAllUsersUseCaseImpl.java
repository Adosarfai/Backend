package com.adosar.backend.business.impl;

import com.adosar.backend.business.GetAllUsersUseCase;
import com.adosar.backend.business.exception.BadRequestException;
import com.adosar.backend.business.request.GetAllUsersRequest;
import com.adosar.backend.business.response.GetAllUsersResponse;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class GetAllUsersUseCaseImpl implements GetAllUsersUseCase {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private UserRepository userRepository;

	@Override
	public GetAllUsersResponse getAllUsers(final GetAllUsersRequest request) {
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
}
