package com.adosar.backend.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "score")
public class ScoreEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "score_id", length = 10, nullable = false, updatable = false)
	public Integer scoreId;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "map", nullable = false, updatable = false)
	public MapEntity map;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "user", nullable = false, updatable = false)
	public UserEntity user;

	@NotNull
	@OneToOne(optional = false)
	@JoinColumn(name = "replay", nullable = false, updatable = false)
	public ReplayEntity replay;

	@PastOrPresent
	@Column(name = "time_set", nullable = false, updatable = false)
	public Date timeSet;

	@NotNull
	@Column(name = "speed", nullable = false, updatable = false)
	public Float speed;

	@NotNull
	@PositiveOrZero
	@Column(name = "points", nullable = false, updatable = false)
	public Integer points;

	@Override
	public String toString() {
		return String.format("(map=%s, user=%s, speed=%s, timeSet=%s, replay=%s, points=%s, scoreId=%s)", map, user, speed, timeSet, replay, points, scoreId);
	}
}
