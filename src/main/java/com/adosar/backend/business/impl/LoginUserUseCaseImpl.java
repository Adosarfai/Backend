package com.adosar.backend.business.impl;

import com.adosar.backend.business.LoginUserUseCase;
import com.adosar.backend.business.exception.FieldNotFoundException;
import com.adosar.backend.controller.request.LoginUserRequest;
import com.adosar.backend.controller.response.LoginUserResponse;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.password4j.Password;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LoginUserUseCaseImpl implements LoginUserUseCase {
    private UserRepository userRepository;

    @Override
    public LoginUserResponse loginUser(final LoginUserRequest request) {
        try {
            User user = UserConverter.convert(userRepository.getUserEntityByEmail(request.getEmail()));

            // Invalid email
            if (user == null) throw new FieldNotFoundException(String.format("User with email %s was not found", request.getEmail()));
            
            // Invalid password
            if (!Password.check(request.getPassword(), user.getPassword()).withArgon2()) throw new CredentialException("Password hashes do not match");
            
            Algorithm algorithm = Algorithm.HMAC512("Adosar");
            String jwt = JWT.create()
                    .withIssuer("Adosar")
                    .withSubject("User auth")
                    .withClaim("UserId", user.getUserId())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(604800))
                    .withJWTId(UUID.randomUUID().toString())
                    .withNotBefore(Instant.now())
                    .sign(algorithm);
            
            return new LoginUserResponse(jwt, HttpStatus.OK);

        } catch (CredentialException credentialException) {
            return new LoginUserResponse(null, HttpStatus.UNAUTHORIZED);
        } catch (FieldNotFoundException fieldNotFoundException) {
            return new LoginUserResponse(null, HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new LoginUserResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
