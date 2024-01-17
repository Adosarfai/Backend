package com.adosar.backend.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Leaderboard {

	@NotNull
	public User user;

	@NotNull
	@Id
	public Integer leaderboardId;

	@NotNull
	@Setter
	public List<Map> maps;

	@NotNull
	@Setter
	public List<User> players;

	@Override
	public String toString() {
		return String.format("(user=%s, leaderboardId=%s, maps=%s, players=%s)", user, leaderboardId, maps, players);
	}
}
