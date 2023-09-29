package com.adosar.backend.business.impl;

import com.adosar.backend.business.CreateNewUserUseCase;
import com.adosar.backend.controller.request.CreateNewUserRequest;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import com.password4j.Hash;
import com.password4j.Password;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
@AllArgsConstructor
public class CreateNewUserUseCaseImpl implements CreateNewUserUseCase {
    private UserRepository userRepository;

    @Override
    public HttpStatus createNewUser(final CreateNewUserRequest request) {
        try {
            Hash hash = Password.hash(request.getPassword())
                    .addRandomSalt(32)
                    .withArgon2();

            UserEntity newUser = UserEntity.builder()
                    .password(hash.getResult())
                    .email(request.getEmail())
                    .privilege(Privilege.USER)
                    .username(request.getUsername())
                    .build();

            userRepository.save(newUser);
            // TODO: Send verification email

            return HttpStatus.CREATED;
        } catch (InvalidParameterException invalidParameterException) {
            return HttpStatus.BAD_REQUEST;
        } catch (Exception exception) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
