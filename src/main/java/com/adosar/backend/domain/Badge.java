package com.adosar.backend.domain;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Badge {

	@Id
	@NotNull
	private Integer badgeId;

	@NotNull
	@Setter
	private String name;
}
