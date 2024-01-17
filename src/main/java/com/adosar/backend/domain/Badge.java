package com.adosar.backend.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Badge {

	@Id
	@NotNull
	public Integer badgeId;

	@NotNull
	@Setter
	public String name;

	@Override
	public String toString() {
		return String.format("(badgeId=%s, name=%s)", badgeId, name);
	}
}
