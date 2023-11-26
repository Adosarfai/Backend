package com.adosar.backend.business.request.score;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GetAllScoresRequest {

	@NotNull
	private Integer page;
}
