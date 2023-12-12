package com.adosar.backend.test;

import com.adosar.backend.persistence.entity.UserEntity;

public class MockCreator {
	public static UserEntity MockUserEntity(Integer userId) {
		userId = userId != null ? userId : 1;
		return UserEntity.builder()
				.userId(userId)
				.build();
	}
}
