package com.adosar.backend.business.converter;

import com.adosar.backend.domain.Score;
import com.adosar.backend.persistence.entity.ScoreEntity;
import com.adosar.backend.test.MockCreator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ScoreConverterTest {

	// score parameters are the same as scoreEntity parameters
	@Test
	void score_parameters_are_the_same_as_score_entity_parameters() throws Exception {
		// Arrange
		ScoreEntity scoreEntity = MockCreator.mockScoreEntity(1);

		// Act
		Score score = ScoreConverter.convert(scoreEntity);

		// Assert
		for (Field field : score.getClass().getDeclaredFields()) {
			if (scoreEntity.getClass().getDeclaredField(field.getName()).get(scoreEntity) == null) {
				assertNull(field.get(score));
				continue;
			}

			System.out.println(scoreEntity.getClass().getDeclaredField(field.getName()).get(scoreEntity).toString());
			System.out.println(field.get(score).toString());

			char[] scoreEntityField = scoreEntity.getClass().getDeclaredField(field.getName()).get(scoreEntity).toString().toCharArray();
			char[] scoreField = field.get(score).toString().toCharArray();
			Arrays.sort(scoreEntityField);
			Arrays.sort(scoreField);

			// funny edgecase where "ab" and "ba" would technically be the same

			assertArrayEquals(scoreEntityField, scoreField);
		}
	}

	// scoreEntity parameters are the same as score parameters
	@Test
	void score_entity_parameters_are_the_same_as_score_parameters() throws Exception {
		// Arrange
		Score score = MockCreator.mockScore(1);

		// Act
		ScoreEntity scoreEntity = ScoreConverter.convert(score);

		// Assert
		for (Field field : scoreEntity.getClass().getDeclaredFields()) {
			if (score.getClass().getDeclaredField(field.getName()).get(score) == null) {
				assertNull(field.get(scoreEntity));
				continue;
			}
			System.out.println(score.getClass().getDeclaredField(field.getName()).get(score).toString());
			System.out.println(field.get(scoreEntity).toString());

			char[] scoreField = score.getClass().getDeclaredField(field.getName()).get(score).toString().toCharArray();
			char[] scoreEntityField = field.get(scoreEntity).toString().toCharArray();
			Arrays.sort(scoreField);
			Arrays.sort(scoreEntityField);

			// funny edgecase where "ab" and "ba" would technically be the same

			assertArrayEquals(scoreField, scoreEntityField);
		}
	}
}