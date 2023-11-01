package com.adosar.backend.business.impl;

import com.adosar.backend.business.LoginUserUseCase;
import com.adosar.backend.business.exception.UnauthorizedException;
import com.adosar.backend.business.request.LoginUserRequest;
import com.adosar.backend.business.response.LoginUserResponse;
import com.adosar.backend.business.service.JWTService;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import com.password4j.Password;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class LoginUserUseCaseImpl implements LoginUserUseCase {
    private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
    private UserRepository userRepository;

    @Override
    public LoginUserResponse loginUser(final LoginUserRequest request) {
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
}
