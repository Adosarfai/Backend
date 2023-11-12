package com.adosar.backend.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Score {

	@NotNull
	private Map map;

	@NotNull
	private User user;

	@NotNull
	private Float speed;

	@NotNull
	private Date timeSet;

	@NotNull
	private Replay replay;

	@NotNull
	private Integer score;

	@NotNull
	@Id
	private Integer scoreId;
}
