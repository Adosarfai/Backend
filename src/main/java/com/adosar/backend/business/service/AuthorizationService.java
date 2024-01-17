package com.adosar.backend.business.service;

import com.adosar.backend.business.converter.UserConverter;
import com.adosar.backend.domain.Authorization;
import com.adosar.backend.domain.Privilege;
import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.UserRepository;
import com.adosar.backend.persistence.entity.UserEntity;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public final class AuthorizationService {

	public static Authorization isAuthorized(String jwt, Privilege privilege, UserRepository userRepository) {
		Optional<UserEntity> userEntity = getUserEntityFromJwt(jwt, userRepository);

		if (userEntity.isEmpty()) {
			return Authorization.builder().isAuthorized(false).build();
		}

		User user = UserConverter.convert(userEntity.get());

		return Authorization.builder()
				.user(user)
				.isAuthorized(userEntity.filter(entity -> entity.getPrivilege() == privilege).isPresent())
				.build();
	}

	public static Authorization isAuthorized(String jwt, Integer requiredUserId, UserRepository userRepository) {
		Optional<UserEntity> userEntity = getUserEntityFromJwt(jwt, userRepository);

		if (userEntity.isEmpty()) {
			return Authorization.builder().isAuthorized(false).build();
		}

		User user = UserConverter.convert(userEntity.get());

		return Authorization.builder()
				.user(user)
				.isAuthorized(userEntity.filter(entity -> Objects.equals(entity.getUserId(), requiredUserId)).isPresent())
				.build();
	}

	private static Optional<UserEntity> getUserEntityFromJwt(String jwt, UserRepository userRepository) {
		if (jwt == null || jwt.isBlank()) return Optional.empty();

		DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
		if (decodedJWT == null) return Optional.empty();
		Integer userId = decodedJWT.getClaim("userId").as(Integer.class);

		if (userId == null || userId <= 0) return Optional.empty();

		return userRepository.getUserEntityByUserId(userId);
	}
}
