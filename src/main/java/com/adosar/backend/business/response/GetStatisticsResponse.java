package com.adosar.backend.business.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class GetStatisticsResponse {

	@NotNull
	private Long MapCount;

	@NotNull
	private Long LeaderboardCount;

	@NotNull
	private Long ScoreCount;

	@NotNull
	@JsonIgnore
	private HttpStatus httpStatus;

}
