package com.adosar.backend.business.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class UserQueryRequest {

	@NotNull
	private String username;

	@NotNull
	private Date before;

	@NotNull
	private Date after;

	@NotNull
	private Integer page;

}
