package com.adosar.backend.test;

import com.adosar.backend.domain.Privilege;
import com.adosar.backend.domain.Removed;
import com.adosar.backend.persistence.entity.BadgeEntity;
import com.adosar.backend.persistence.entity.MapEntity;
import com.adosar.backend.persistence.entity.UserEntity;

import java.sql.Date;
import java.time.Instant;
import java.util.Set;

public class MockCreator {

	public static MapEntity mockMapEntity(Integer mapId) {
		return MapEntity.builder()
				.user(mockUserEntity(1))
				.mapId(mapId)
				.creationDate(Date.from(Instant.now()))
				.hash("5d5b09f6dcb2d53a5fffc60c4ac0d55fabdf556069d6631545f42aa6e3500f2e")
				.title(String.format("Mocked test map %s", mapId))
				.artist(String.format("Mocked test map artist %s", mapId))
				.lastUpdate(Date.from(Instant.now()))
				.removed(Removed.NOT_REMOVED)
				.published(true)
				.removalReason(null)
				.build();
	}

	public static UserEntity mockUserEntity(Integer userId) {
		return UserEntity.builder()
				.userId(userId)
				.creationDate(Date.from(Instant.now()))
				.username(String.format("Mocked test user %s", userId))
				.description(String.format("Mocked test user description %s", userId))
				.privilege(Privilege.USER)
				.badges(Set.of(
						mockBadgeEntity(1),
						mockBadgeEntity(2),
						mockBadgeEntity(3)
				))
				.email(String.format("%stestmail@mail.com", userId))
				.password("$argon2id$v=19$m=4096,t=30,p=4$dHsmibZbO/Uy+vd7afaDhyPYVrtUmKnmZid/QquNBN8$wRj2/CrAlxxDSX1rCijssPWVRdLxzahuVjSvOUHCrgbHqhxdbFmQ0G0NkK/dOqilDMDu4+o6E+C3udJUcbLfWg")
				.build();
	}

	public static BadgeEntity mockBadgeEntity(Integer badgeId) {
		return BadgeEntity.builder()
				.badgeId(badgeId)
				.name(String.format("Mocked test badge %s", badgeId))
				.build();
	}
}
