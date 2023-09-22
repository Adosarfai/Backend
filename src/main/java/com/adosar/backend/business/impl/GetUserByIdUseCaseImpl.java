package com.adosar.backend.business.impl;

import com.adosar.backend.business.GetAllUsersUseCase;
import com.adosar.backend.business.GetUserByIdUseCase;
import com.adosar.backend.business.exception.IdNotFoundException;
import com.adosar.backend.business.exception.InvalidPathVariableException;
import com.adosar.backend.domain.User;
import com.adosar.backend.domain.request.GetAllUsersRequest;
import com.adosar.backend.domain.request.GetUserByIdRequest;
import com.adosar.backend.domain.response.GetAllUsersResponse;
import com.adosar.backend.domain.response.GetUserByIdResponse;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetUserByIdUseCaseImpl implements GetUserByIdUseCase {
    private UserRepository userRepository;
    
    @Override
    public GetUserByIdResponse getUserById(final GetUserByIdRequest request) {
        try {
            if (request.getId() < 0) throw new InvalidPathVariableException(request.getId().toString());

            UserEntity result = userRepository.findById(request.getId()).orElseThrow(() -> new IdNotFoundException(request.getId(), "user"));
            User user = UserConverter.convert(result);

            return new GetUserByIdResponse(user, HttpStatus.OK);

        } catch (InvalidPathVariableException invalidPathVariableException) {
            return new GetUserByIdResponse(null, HttpStatus.BAD_REQUEST);
        } catch (IdNotFoundException idNotFoundException) {
            return new GetUserByIdResponse(null, HttpStatus.NOT_FOUND);
        }  catch (Exception exception) {
            return new GetUserByIdResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
