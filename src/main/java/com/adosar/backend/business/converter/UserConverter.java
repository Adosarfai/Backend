package com.adosar.backend.business.converter;

import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@NoArgsConstructor
public final class UserConverter {

	public static UserEntity convert(User user) {
		assert user != null;
		return UserEntity.builder()
				.email(user.getEmail())
				.userId(user.getUserId())
				.username(user.getUsername())
				.description(user.getDescription())
				.password(user.getPassword())
				.privilege(user.getPrivilege())
				.creationDate(user.getCreationDate())
				.badges(user.getBadges().stream().map(BadgeConverter::convert).collect(Collectors.toSet()))
				.build();
	}

	public static User convert(UserEntity user) {
		assert user != null;
		return User.builder()
				.email(user.getEmail())
				.userId(user.getUserId())
				.username(user.getUsername())
				.description(user.getDescription())
				.password(user.getPassword())
				.privilege(user.getPrivilege())
				.creationDate(user.getCreationDate())
				.badges(user.getBadges().stream().map(BadgeConverter::convert).collect(Collectors.toSet()))
				.build();
	}
}
