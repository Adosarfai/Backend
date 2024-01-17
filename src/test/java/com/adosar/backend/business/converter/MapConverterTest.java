package com.adosar.backend.business.converter;

import com.adosar.backend.domain.Map;
import com.adosar.backend.persistence.entity.MapEntity;
import com.adosar.backend.test.MockCreator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MapConverterTest {

	// map parameters are the same as mapEntity parameters
	@Test
	void map_parameters_are_the_same_as_map_entity_parameters() throws Exception {
		// Arrange
		MapEntity mapEntity = MockCreator.mockMapEntity(1);

		// Act
		Map map = MapConverter.convert(mapEntity);

		// Assert
		for (Field field : map.getClass().getDeclaredFields()) {
			if (mapEntity.getClass().getDeclaredField(field.getName()).get(mapEntity) == null) {
				assertNull(field.get(map));
				continue;
			}

			System.out.println(mapEntity.getClass().getDeclaredField(field.getName()).get(mapEntity).toString());
			System.out.println(field.get(map).toString());

			char[] mapEntityField = mapEntity.getClass().getDeclaredField(field.getName()).get(mapEntity).toString().toCharArray();
			char[] mapField = field.get(map).toString().toCharArray();
			Arrays.sort(mapEntityField);
			Arrays.sort(mapField);

			// funny edgecase where "ab" and "ba" would technically be the same

			assertArrayEquals(mapEntityField, mapField);
		}
	}

	// mapEntity parameters are the same as map parameters
	@Test
	void map_entity_parameters_are_the_same_as_map_parameters() throws Exception {
		// Arrange
		Map map = MockCreator.mockMap(1);

		// Act
		MapEntity mapEntity = MapConverter.convert(map);

		// Assert
		for (Field field : mapEntity.getClass().getDeclaredFields()) {
			if (map.getClass().getDeclaredField(field.getName()).get(map) == null) {
				assertNull(field.get(mapEntity));
				continue;
			}

			System.out.println(map.getClass().getDeclaredField(field.getName()).get(map).toString());
			System.out.println(field.get(mapEntity).toString());


			char[] mapField = map.getClass().getDeclaredField(field.getName()).get(map).toString().toCharArray();
			char[] mapEntityField = field.get(mapEntity).toString().toCharArray();
			Arrays.sort(mapField);
			Arrays.sort(mapEntityField);

			// funny edgecase where "ab" and "ba" would technically be the same

			assertArrayEquals(mapField, mapEntityField);
		}
	}
}