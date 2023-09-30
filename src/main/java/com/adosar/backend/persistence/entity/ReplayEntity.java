package com.adosar.backend.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "replay")
public class ReplayEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", length = 10, nullable = false, updatable = false)
	private Integer replayId;

	@NotNull
	@Column(name = "timings", nullable = false, updatable = false)
	private int[] timings;

	@NotNull
	@Column(name = "pauses", nullable = false, updatable = false)
	private int[] pauses;
}
