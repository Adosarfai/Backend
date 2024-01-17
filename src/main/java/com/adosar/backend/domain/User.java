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
	public Integer userId;

	@NotNull
	public Date creationDate;

	@Setter
	@NotNull
	public String username;

	@Setter
	@NotNull
	public String description;

	@Setter
	@NotNull
	public Privilege privilege;

	@Setter
	@NotNull
	public Set<Badge> badges;

	@JsonIgnore
	@Setter
	@NotNull
	public String email;

	@JsonIgnore
	@Setter
	@NotNull
	public String password;

	@Override
	public String toString() {
		return String.format("(userId=%s, creationDate=%s, username=%s, description=%s, privilege=%s, badges=%s, email=%s, password=%s)", userId, creationDate, username, description, privilege, badges, email, password);
	}
}
