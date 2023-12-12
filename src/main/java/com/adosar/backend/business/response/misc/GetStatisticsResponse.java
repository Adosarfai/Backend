package com.adosar.backend.business.response.misc;

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
	private Integer mapCount;

	@NotNull
	private Integer leaderboardCount;

	@NotNull
	private Integer scoreCount;

	@NotNull
	@JsonIgnore
	private HttpStatus httpStatus;

}
