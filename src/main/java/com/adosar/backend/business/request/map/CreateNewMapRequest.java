package com.adosar.backend.business.request.map;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
public class CreateNewMapRequest {

	@Setter
	@NotNull
	private String title;

	@Setter
	@NotNull
	private String artist;

	@Setter
	@NotNull
	private Boolean published;

}
