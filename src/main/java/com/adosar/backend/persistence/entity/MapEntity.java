package com.adosar.backend.persistence.entity;

import com.adosar.backend.domain.Removed;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "map")
public class MapEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "map_id", length = 10, nullable = false, updatable = false)
	private Integer mapId;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "user", nullable = false, updatable = false)
	private UserEntity user;

	@NotBlank
	@NotEmpty
	@Size(max = 250)
	@Length(max = 250)
	@NotNull
	@Column(name = "title", length = 250, nullable = false)
	private String title;

	@Length(max = 100)
	@Size(max = 100)
	@NotBlank
	@NotEmpty
	@NotNull
	@Column(name = "artist", length = 100, nullable = false)
	private String artist;

	@NotNull
	@Builder.Default
	@Column(name = "published", nullable = false)
	private Boolean published = false;

	@NotNull
	@Enumerated
	@Builder.Default
	@Column(name = "removed", nullable = false)
	private Removed removed = Removed.NOT_REMOVED;

	@Length(max = 200)
	@Size(max = 2000)
	@Column(name = "removal_reason", length = 200)
	private String removalReason;

	@Length(min = 64, max = 64)
	@Size(min = 64, max = 64)
	@Nullable
	@Column(name = "hash", length = 64)
	private String hash;

	@PastOrPresent
	@NotNull
	@CreationTimestamp
	@Column(name = "creation_date", nullable = false, updatable = false)
	private Date creationDate;

	@PastOrPresent
	@NotNull
	@UpdateTimestamp
	@Column(name = "last_update", nullable = false)
	private Date lastUpdate;

}
