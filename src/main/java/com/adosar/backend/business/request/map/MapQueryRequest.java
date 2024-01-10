package com.adosar.backend.business.request.map;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class MapQueryRequest {

	@NotNull
	private String title;

	@NotNull
	private Date before;

	@NotNull
	private Date after;

	@NotNull
	private Integer page;

}
