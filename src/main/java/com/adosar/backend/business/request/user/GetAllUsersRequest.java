package com.adosar.backend.business.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GetAllUsersRequest {

	@NotNull
	private Integer page;
}
