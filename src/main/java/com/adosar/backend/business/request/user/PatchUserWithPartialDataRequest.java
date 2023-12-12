package com.adosar.backend.business.request.user;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserWithPartialDataRequest {

	@Nullable
	private String username;

	@Nullable
	private String email;

	@Nullable
	private String password;

	@Nullable
	private String profilePicture;

}
