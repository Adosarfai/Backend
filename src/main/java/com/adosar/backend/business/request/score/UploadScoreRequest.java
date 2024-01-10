package com.adosar.backend.business.request.score;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UploadScoreRequest {

	@NotNull
	private Integer mapId;

	@NotNull
	private Integer userId;

	@NotNull
	private int[] timings;

	@NotNull
	private int[] pauses;

	@NotNull
	private float speed;

	@NotNull
	private Integer points;
}
