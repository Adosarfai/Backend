package com.adosar.backend.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Leaderboard {

	@NotNull
	private User user;

	@NotNull
	@Id
	private Integer leaderboardId;

	@NotNull
	@Setter
	private List<Map> maps;

	@NotNull
	@Setter
	private List<User> players;
}
