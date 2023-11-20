package com.adosar.backend.business.converter;

import com.adosar.backend.domain.Badge;
import com.adosar.backend.persistence.entity.BadgeEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class BadgeConverter {

	public static BadgeEntity convert(Badge badge) {
		assert badge != null;
		return BadgeEntity.builder()
				.badgeId(badge.getBadgeId())
				.name(badge.getName())
				.build();
	}

	public static Badge convert(BadgeEntity badge) {
		assert badge != null;
		return Badge.builder()
				.badgeId(badge.getBadgeId())
				.name(badge.getName())
				.build();
	}
}
