package com.adosar.backend.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pack {

	@NotNull
	public User user;

	@NotNull
	@Id
	public Integer packId;

	@Setter
	@NotNull
	public String title;

	@Setter
	@NotNull
	public List<Map> maps;

	@Setter
	@NotNull
	public Removed removed;

	@Setter
	@NotNull
	public boolean published;

	@Setter
	@Nullable
	public String removalReason;

	@Override
	public String toString() {
		return String.format("(user=%s, packId=%s, title=%s, maps=%s, removed=%s, published=%s, removalReason=%s)", user, packId, title, maps, removed, published, removalReason);
	}
}
