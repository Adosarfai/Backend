package com.adosar.backend.persistence.entity;

import com.adosar.backend.domain.Removed;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;


@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pack")
public class PackEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pack_id", length = 10, nullable = false, updatable = false)
	public Integer packId;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "user", nullable = false, updatable = false)
	public UserEntity user;

	@Length(max = 250)
	@Size(max = 250)
	@NotBlank
	@NotEmpty
	@NotNull
	@Column(name = "title", length = 250, nullable = false)
	public String title;

	@NotNull
	@Builder.Default
	@Column(name = "published", nullable = false)
	public Boolean published = false;

	@NotNull
	@Enumerated
	@Builder.Default
	@Column(name = "removed", nullable = false)
	public Removed removed = Removed.NOT_REMOVED;

	@Length(max = 2000)
	@Size(max = 2000)
	@Column(name = "removal_reason", length = 2000)
	public String removalReason;

	@Size(min = 1)
	@NotNull
	@ManyToMany
	public List<MapEntity> maps;

	@Override
	public String toString() {
		return String.format("(user=%s, packId=%s, title=%s, maps=%s, removed=%s, published=%s, removalReason=%s)", user, packId, title, maps, removed, published, removalReason);
	}
}
