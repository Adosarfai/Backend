package com.adosar.backend.business.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMapsByUserIdRequest {

	@NotNull
	private Integer page;
	
	@NotNull
	private Integer userId;
}