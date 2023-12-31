package com.adosar.backend.business.response.user;

import com.adosar.backend.domain.User;
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
public class GetAllUsersResponse {

	@Nullable
	private Iterable<User> users;

	@NotNull
	@JsonIgnore
	private HttpStatus httpStatus;


}
