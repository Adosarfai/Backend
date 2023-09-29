package com.adosar.backend.business.impl;

import com.adosar.backend.business.GetAllUsersUseCase;
import com.adosar.backend.business.exception.InvalidPathVariableException;
import com.adosar.backend.controller.request.GetAllUsersRequest;
import com.adosar.backend.controller.response.GetAllUsersResponse;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllUsersUseCaseImpl implements GetAllUsersUseCase {
    private UserRepository userRepository;

    @Override
    public GetAllUsersResponse getAllUsers(final GetAllUsersRequest request) {
        try {
            if (request.getPage() < 0) throw new InvalidPathVariableException(request.getPage().toString());

            List<UserEntity> result = userRepository.findAll(PageRequest.of(request.getPage(), 10)).toList();
            List<User> users = result.stream().map(UserConverter::convert).toList();

            return new GetAllUsersResponse(users, HttpStatus.OK);
        } catch (InvalidPathVariableException invalidPathVariableException) {
            return new GetAllUsersResponse(null, HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            return new GetAllUsersResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
