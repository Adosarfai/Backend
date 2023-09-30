package com.adosar.backend.business.impl;

import com.adosar.backend.business.CreateNewUserUseCase;
import com.adosar.backend.business.request.CreateNewUserRequest;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import com.password4j.BadParametersException;
import com.password4j.Hash;
import com.password4j.Password;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class CreateNewUserUseCaseImpl implements CreateNewUserUseCase {
    private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
    private UserRepository userRepository;

    @Override
    public HttpStatus createNewUser(@Valid final CreateNewUserRequest request) {
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
        } catch (BadParametersException | InvalidParameterException badParametersException) {
            LOGGER.log(Level.FINE, badParametersException.toString(), badParametersException);
            return HttpStatus.BAD_REQUEST;
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
