package com.adosar.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@NotNull
	@Id
	private Integer userId;

	@NotNull
	private Date creationDate;

	@Setter
	@NotNull
	private String username;

	@Setter
	@NotNull
	private Privilege privilege;

	@JsonIgnore
	@Setter
	@NotNull
	private String email;

	@JsonIgnore
	@Setter
	@NotNull
	private String password;
}
