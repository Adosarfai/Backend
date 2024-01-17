package com.adosar.backend.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "leaderboard_id", length = 10, nullable = false, updatable = false)
	public Integer leaderboardId;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "user", nullable = false, updatable = false)
	public UserEntity user;

	@Size(min = 1)
	@NotNull
	@ManyToMany
	public List<MapEntity> maps;

	@Size
	@NotNull
	@ManyToMany
	public List<UserEntity> players;

	@Override
	public String toString() {
		return String.format("(user=%s, leaderboardId=%s, maps=%s, players=%s)", user, leaderboardId, maps, players);
	}
}
