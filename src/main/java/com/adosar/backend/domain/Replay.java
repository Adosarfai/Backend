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
public class Replay {

	@NotNull
	@Id
	private Integer replayId;

	@NotNull
	private List<Integer> pauses;

	@NotNull
	private List<Integer> timings;
}
