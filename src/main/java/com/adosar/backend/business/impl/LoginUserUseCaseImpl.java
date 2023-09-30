package com.adosar.backend.business.impl;

import com.adosar.backend.business.LoginUserUseCase;
import com.adosar.backend.business.request.LoginUserRequest;
import com.adosar.backend.business.response.LoginUserResponse;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.password4j.Password;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.time.Instant;
import java.util.UUID;
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
			UserEntity userEntity = userRepository.getUserEntityByEmail(request.getEmail());

			// Invalid email
			if (userEntity == null)
				throw new CredentialException(String.format("User with email %s was not found", request.getEmail()));

			User user = UserConverter.convert(userEntity);

			// Invalid password
			if (!Password.check(request.getPassword(), user.getPassword()).withArgon2())
				throw new CredentialException("Password hashes do not match");

			Algorithm algorithm = Algorithm.HMAC512(System.getenv("HMAC512_SECRET"));
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
			LOGGER.log(Level.FINE, credentialException.toString(), credentialException);
			return new LoginUserResponse(null, HttpStatus.UNAUTHORIZED);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new LoginUserResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
