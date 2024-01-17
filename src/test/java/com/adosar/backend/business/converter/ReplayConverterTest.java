package com.adosar.backend.business.converter;

import com.adosar.backend.domain.Replay;
import com.adosar.backend.persistence.entity.ReplayEntity;
import com.adosar.backend.test.MockCreator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReplayConverterTest {

	// replay parameters are the same as replayEntity parameters
	@Test
	void replay_parameters_are_the_same_as_replay_entity_parameters() throws Exception {
		// Arrange
		ReplayEntity replayEntity = MockCreator.mockReplayEntity(1);

		// Act
		Replay replay = ReplayConverter.convert(replayEntity);

		// Assert
		for (Field field : replay.getClass().getDeclaredFields()) {
			if (replayEntity.getClass().getDeclaredField(field.getName()).get(replayEntity) == null) {
				assertNull(field.get(replay));
				continue;
			}

			// Checking for int[] because i can't override a primitive toString
			if (replayEntity.getClass().getDeclaredField(field.getName()).getType().toString().equals("class [I")) {
				System.out.println(Arrays.toString((int[]) replayEntity.getClass().getDeclaredField(field.getName()).get(replayEntity)));
				System.out.println(field.get(replay).toString());

				char[] replayEntityField = Arrays.toString((int[]) replayEntity.getClass().getDeclaredField(field.getName()).get(replayEntity)).toCharArray();
				char[] replayField = field.get(replay).toString().toCharArray();
				Arrays.sort(replayEntityField);
				Arrays.sort(replayField);

				assertArrayEquals(replayEntityField, replayField);
				continue;
			}

			System.out.println(replayEntity.getClass().getDeclaredField(field.getName()).get(replayEntity).toString());
			System.out.println(field.get(replay).toString());

			char[] replayEntityField = replayEntity.getClass().getDeclaredField(field.getName()).get(replayEntity).toString().toCharArray();
			char[] replayField = field.get(replay).toString().toCharArray();
			Arrays.sort(replayEntityField);
			Arrays.sort(replayField);

			// funny edgecase where "ab" and "ba" would technically be the same

			assertArrayEquals(replayEntityField, replayField);
		}
	}

	// replayEntity parameters are the same as replay parameters
	@Test
	void replay_entity_parameters_are_the_same_as_replay_parameters() throws Exception {
		// Arrange
		Replay replay = MockCreator.mockReplay(1);

		// Act
		ReplayEntity replayEntity = ReplayConverter.convert(replay);

		// Assert
		for (Field field : replayEntity.getClass().getDeclaredFields()) {
			if (replay.getClass().getDeclaredField(field.getName()).get(replay) == null) {
				assertNull(field.get(replayEntity));
				continue;
			}

			// Checking for int[] because i can't override a primitive toString
			if (field.getType().toString().equals("class [I")) {
				System.out.println(Arrays.toString((int[]) field.get(replayEntity)));
				System.out.println(replay.getClass().getDeclaredField(field.getName()).get(replay).toString());

				char[] replayEntityField = Arrays.toString((int[]) field.get(replayEntity)).toCharArray();
				char[] replayField = replay.getClass().getDeclaredField(field.getName()).get(replay).toString().toCharArray();
				Arrays.sort(replayEntityField);
				Arrays.sort(replayField);

				assertArrayEquals(replayEntityField, replayField);
				continue;
			}

			System.out.println(replay.getClass().getDeclaredField(field.getName()).get(replay).toString());
			System.out.println(field.get(replayEntity).toString());

			char[] replayField = replay.getClass().getDeclaredField(field.getName()).get(replay).toString().toCharArray();
			char[] replayEntityField = field.get(replayEntity).toString().toCharArray();
			Arrays.sort(replayField);
			Arrays.sort(replayEntityField);

			// funny edgecase where "ab" and "ba" would technically be the same

			assertArrayEquals(replayField, replayEntityField);
		}
	}
}