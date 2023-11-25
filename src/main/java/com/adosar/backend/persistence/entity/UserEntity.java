package com.adosar.backend.persistence.entity;

import com.adosar.backend.domain.Privilege;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.Set;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", length = 10, nullable = false, updatable = false)
	private Integer userId;

	@Length(min = 3, max = 50)
	@Size(min = 3, max = 50)
	@NotBlank
	@NotEmpty
	@NotNull
	@Column(name = "username", length = 50, nullable = false)
	@Setter
	private String username;

	@Email(regexp = ".+@.+\\..+")
	@NotBlank
	@NotEmpty
	@NotNull
	@JsonIgnore
	@Column(name = "email", nullable = false)
	@Setter
	private String email;

	@Length(max = 200)
	@Size(max = 200)
	@NotBlank
	@NotEmpty
	@NotNull
	@JsonIgnore
	@Column(name = "password", length = 200, nullable = false)
	@Setter
	private String password;

	@Length(max = 2000)
	@Size(max = 2000)
	@NotBlank
	@NotEmpty
	@NotNull
	@Builder.Default
	@Column(name = "description", length = 2000, nullable = false)
	@Setter
	private String description = "I am new to Adosar!";

	@NotNull
	@ManyToMany
	@Setter
	@JoinColumn(nullable = false)
	private Set<BadgeEntity> badges;

	@NotNull
	@Enumerated
	@Column(name = "privilege", nullable = false)
	private Privilege privilege;

	@NotNull
	@PastOrPresent
	@CreationTimestamp
	@Column(name = "creation_date", nullable = false, updatable = false)
	private Date creationDate;
}
