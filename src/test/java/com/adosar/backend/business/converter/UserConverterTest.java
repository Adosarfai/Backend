package com.adosar.backend.business.converter;

import com.adosar.backend.domain.User;
import com.adosar.backend.persistence.entity.UserEntity;
import com.adosar.backend.test.MockCreator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserConverterTest {

	// user parameters are the same as userEntity parameters
	@Test
	void user_parameters_are_the_same_as_user_entity_parameters() throws Exception {
		// Arrange
		UserEntity userEntity = MockCreator.mockUserEntity(1);

		// Act
		User user = UserConverter.convert(userEntity);

		// Assert
		for (Field field : user.getClass().getDeclaredFields()) {
			if (userEntity.getClass().getDeclaredField(field.getName()).get(userEntity) == null) {
				assertNull(field.get(user));
				continue;
			}

			System.out.println(userEntity.getClass().getDeclaredField(field.getName()).get(userEntity).toString());
			System.out.println(field.get(user).toString());

			char[] userEntityField = userEntity.getClass().getDeclaredField(field.getName()).get(userEntity).toString().toCharArray();
			char[] userField = field.get(user).toString().toCharArray();
			Arrays.sort(userEntityField);
			Arrays.sort(userField);

			// funny edgecase where "ab" and "ba" would technically be the same

			assertArrayEquals(userEntityField, userField);
		}
	}

	// userEntity parameters are the same as user parameters
	@Test
	void user_entity_parameters_are_the_same_as_user_parameters() throws Exception {
		// Arrange
		User user = MockCreator.mockUser(1);

		// Act
		UserEntity userEntity = UserConverter.convert(user);

		// Assert
		for (Field field : userEntity.getClass().getDeclaredFields()) {
			if (user.getClass().getDeclaredField(field.getName()).get(user) == null) {
				assertNull(field.get(userEntity));
				continue;
			}
			System.out.println(user.getClass().getDeclaredField(field.getName()).get(user).toString());
			System.out.println(field.get(userEntity).toString());

			char[] userField = user.getClass().getDeclaredField(field.getName()).get(user).toString().toCharArray();
			char[] userEntityField = field.get(userEntity).toString().toCharArray();
			Arrays.sort(userField);
			Arrays.sort(userEntityField);

			// funny edgecase where "ab" and "ba" would technically be the same

			assertArrayEquals(userField, userEntityField);
		}
	}
}