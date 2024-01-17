package com.adosar.backend.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Score {

	@NotNull
	public Map map;

	@NotNull
	public User user;

	@NotNull
	public Float speed;

	@NotNull
	public Date timeSet;

	@NotNull
	public Replay replay;

	@NotNull
	public Integer points;

	@NotNull
	@Id
	public Integer scoreId;

	@Override
	public String toString() {
		return String.format("(map=%s, user=%s, speed=%s, timeSet=%s, replay=%s, points=%s, scoreId=%s)", map, user, speed, timeSet, replay, points, scoreId);
	}
}
