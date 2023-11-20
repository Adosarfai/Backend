package com.adosar.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.Set;

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
	private String description;

	@Setter
	@NotNull
	private Privilege privilege;

	@Setter
	@NotNull
	private Set<Badge> badges;

	@JsonIgnore
	@Setter
	@NotNull
	private String email;

	@JsonIgnore
	@Setter
	@NotNull
	private String password;
}
