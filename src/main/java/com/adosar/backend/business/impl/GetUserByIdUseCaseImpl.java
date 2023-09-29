package com.adosar.backend.business.impl;

import com.adosar.backend.business.GetUserByIdUseCase;
import com.adosar.backend.business.exception.FieldNotFoundException;
import com.adosar.backend.business.exception.InvalidPathVariableException;
import com.adosar.backend.controller.request.GetUserByIdRequest;
import com.adosar.backend.controller.response.GetUserByIdResponse;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetUserByIdUseCaseImpl implements GetUserByIdUseCase {
    private UserRepository userRepository;

    @Override
    public GetUserByIdResponse getUserById(final GetUserByIdRequest request) {
        try {
            if (request.getId() < 0) throw new InvalidPathVariableException(request.getId().toString());

            UserEntity result = userRepository.findById(request.getId()).orElseThrow(() -> new FieldNotFoundException(String.format("User with ID %s was not found", request.getId())));
            User user = UserConverter.convert(result);

            return new GetUserByIdResponse(user, HttpStatus.OK);

        } catch (InvalidPathVariableException invalidPathVariableException) {
            return new GetUserByIdResponse(null, HttpStatus.BAD_REQUEST);
        } catch (FieldNotFoundException fieldNotFoundException) {
            return new GetUserByIdResponse(null, HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new GetUserByIdResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
