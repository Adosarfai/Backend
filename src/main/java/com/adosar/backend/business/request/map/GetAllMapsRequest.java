package com.adosar.backend.business.request.map;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GetAllMapsRequest {

	@NotNull
	private Integer page;
}
