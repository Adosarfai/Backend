package com.adosar.backend.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
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
	@Column(name = "id", length = 10, nullable = false, updatable = false)
	private Integer scoreId;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "map", nullable = false, updatable = false)
	private MapEntity map;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "user", nullable = false, updatable = false)
	private UserEntity user;

	@NotNull
	@OneToOne(optional = false)
	@JoinColumn(name = "replay", nullable = false, updatable = false)
	private ReplayEntity replay;

	@PastOrPresent
	@Column(name = "time_set", nullable = false, updatable = false)
	private Date timeSet;

	@NotNull
	@Column(name = "speed", nullable = false, updatable = false)
	private Float speed;

	@NotNull
	@PositiveOrZero
	@Column(name = "score", nullable = false, updatable = false)
	private Integer score;
}
