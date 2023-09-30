package com.adosar.backend.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "leaderboard")
public class LeaderboardEntity {
	@Positive
	@NotNull
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 10, nullable = false, updatable = false)
	private Integer leaderboardId;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "user", nullable = false, updatable = false)
	private UserEntity user;

	@Size(min = 1)
	@NotNull
	@ManyToMany
	private List<MapEntity> maps;

	@Size
	@NotNull
	@ManyToMany
	private List<UserEntity> players;
}
