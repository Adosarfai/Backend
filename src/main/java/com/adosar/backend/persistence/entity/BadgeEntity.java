package com.adosar.backend.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "badge")
public class BadgeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "badgeId", length = 10, nullable = false, updatable = false)
	private Integer badgeId;

	@Length(max = 100)
	@Size(max = 100)
	@NotBlank
	@NotEmpty
	@NotNull
	@Column(name = "name", length = 100, nullable = false)
	@Setter
	private String name;
}
