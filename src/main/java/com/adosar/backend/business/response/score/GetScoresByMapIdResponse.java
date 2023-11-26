package com.adosar.backend.business.response.score;

import com.adosar.backend.domain.Score;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetScoresByMapIdResponse {

	@Nullable
	private Iterable<Score> scores;

	@NotNull
	@JsonIgnore
	private HttpStatus httpStatus;

}
