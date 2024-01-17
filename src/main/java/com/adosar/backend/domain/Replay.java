package com.adosar.backend.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Replay {

	@NotNull
	@Id
	public Integer replayId;

	@NotNull
	public List<Integer> pauses;

	@NotNull
	public List<Integer> timings;

	@Override
	public String toString() {
		return String.format("(replayId=%s, pauses=%s, timings=%s)", replayId, pauses, timings);
	}
}
