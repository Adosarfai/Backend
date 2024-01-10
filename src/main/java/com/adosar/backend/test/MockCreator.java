package com.adosar.backend.test;

import com.adosar.backend.domain.Privilege;
import com.adosar.backend.domain.Removed;
import com.adosar.backend.persistence.entity.*;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
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
	
	public static LeaderboardEntity mockLeaderboardEntity(Integer leaderboardId) {
		return LeaderboardEntity.builder()
				.user(mockUserEntity(1))
				.leaderboardId(leaderboardId)
				.maps(List.of(
						mockMapEntity(1),
						mockMapEntity(2),
						mockMapEntity(3)
				))
				.players(List.of(
						mockUserEntity(1),
						mockUserEntity(2),
						mockUserEntity(3)
				))
				.build();
	}
	
	public static PackEntity mockPackEntity(Integer packId) {
		return PackEntity.builder()
				.user(mockUserEntity(1))
				.packId(packId)
				.title(String.format("Mocked test pack %s", packId))
				.maps(List.of(
						mockMapEntity(1),
						mockMapEntity(2),
						mockMapEntity(3)
				))
				.removed(Removed.NOT_REMOVED)
				.published(true)
				.removalReason(null)
				.build();
	}
	
	public static ReplayEntity mockReplayEntity(Integer replayId) {
		return ReplayEntity.builder()
				.replayId(replayId)
				.pauses(new int[] {123, 456, 789})
				.timings(new int[] {123, 456, 789})
				.build();
	}
	
	public static ScoreEntity mockScoreEntity(Integer scoreId) {
		return ScoreEntity.builder()
				.map(mockMapEntity(1))
				.user(mockUserEntity(1))
				.speed(1f)
				.timeSet(Date.from(Instant.now()))
				.replay(mockReplayEntity(1))
				.points(123456)
				.scoreId(scoreId)
				.build();
	}
}
