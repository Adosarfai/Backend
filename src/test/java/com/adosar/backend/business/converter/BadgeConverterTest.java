package com.adosar.backend.business.converter;

import com.adosar.backend.domain.Badge;
import com.adosar.backend.persistence.entity.BadgeEntity;
import com.adosar.backend.test.MockCreator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BadgeConverterTest {

	// badge parameters are the same as badgeEntity parameters
	@Test
	void badge_parameters_are_the_same_as_badge_entity_parameters() throws Exception {
		// Arrange
		BadgeEntity badgeEntity = MockCreator.mockBadgeEntity(1);

		// Act
		Badge badge = BadgeConverter.convert(badgeEntity);

		// Assert
		for (Field field : badge.getClass().getDeclaredFields()) {
			if (badgeEntity.getClass().getDeclaredField(field.getName()).get(badgeEntity) == null) {
				assertNull(field.get(badge));
				continue;
			}

			System.out.println(badgeEntity.getClass().getDeclaredField(field.getName()).get(badgeEntity).toString());
			System.out.println(field.get(badge).toString());

			char[] badgeEntityField = badgeEntity.getClass().getDeclaredField(field.getName()).get(badgeEntity).toString().toCharArray();
			char[] badgeField = field.get(badge).toString().toCharArray();
			Arrays.sort(badgeEntityField);
			Arrays.sort(badgeField);

			// funny edgecase where "ab" and "ba" would technically be the same

			assertArrayEquals(badgeEntityField, badgeField);
		}
	}

	// badgeEntity parameters are the same as badge parameters
	@Test
	void badge_entity_parameters_are_the_same_as_badge_parameters() throws Exception {
		// Arrange
		Badge badge = MockCreator.mockBadge(1);

		// Act
		BadgeEntity badgeEntity = BadgeConverter.convert(badge);

		// Assert
		for (Field field : badgeEntity.getClass().getDeclaredFields()) {
			if (badge.getClass().getDeclaredField(field.getName()).get(badge) == null) {
				assertNull(field.get(badgeEntity));
				continue;
			}

			System.out.println(badge.getClass().getDeclaredField(field.getName()).get(badge).toString());
			System.out.println(field.get(badgeEntity).toString());

			char[] badgeField = badge.getClass().getDeclaredField(field.getName()).get(badge).toString().toCharArray();
			char[] badgeEntityField = field.get(badgeEntity).toString().toCharArray();
			Arrays.sort(badgeField);
			Arrays.sort(badgeEntityField);

			// funny edgecase where "ab" and "ba" would technically be the same

			assertArrayEquals(badgeField, badgeEntityField);
		}
	}
}