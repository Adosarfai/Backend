package com.adosar.backend.business.converter;

import com.adosar.backend.domain.Map;
import com.adosar.backend.persistence.entity.MapEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class MapConverter {

	public static MapEntity convert(Map map) {
		return MapEntity.builder()
				.user(UserConverter.convert(map.getUser()))
				.mapId(map.getMapId())
				.creationDate(map.getCreationDate())
				.hash(map.getHash())
				.title(map.getTitle())
				.artist(map.getArtist())
				.lastUpdate(map.getLastUpdate())
				.removed(map.getRemoved())
				.published(map.getPublished())
				.removalReason(map.getRemovalReason())
				.build();
	}

	public static Map convert(MapEntity map) {
		return Map.builder()
				.user(UserConverter.convert(map.getUser()))
				.mapId(map.getMapId())
				.creationDate(map.getCreationDate())
				.hash(map.getHash())
				.title(map.getTitle())
				.artist(map.getArtist())
				.lastUpdate(map.getLastUpdate())
				.removed(map.getRemoved())
				.published(map.getPublished())
				.removalReason(map.getRemovalReason())
				.build();
	}
}
