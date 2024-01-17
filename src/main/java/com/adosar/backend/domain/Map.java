package com.adosar.backend.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Map {

	@NotNull
	public User user;

	@NotNull
	@Id
	public Integer mapId;

	@NotNull
	public Date creationDate;

	@Setter
	@Nullable
	public String hash;

	@Setter
	@NotNull
	public String title;

	@Setter
	@NotNull
	public String artist;

	@Setter
	@NotNull
	public Date lastUpdate;

	@Setter
	@NotNull
	public Removed removed;

	@Setter
	@NotNull
	public Boolean published;

	@Setter
	@Nullable
	public String removalReason;

	@Override
	public String toString() {
		return String.format("(user=%s, mapId=%s, creationDate=%s, hash=%s, title=%s, artist=%s, lastUpdate=%s, removed=%s, published=%s, removalReason=%s)", user, mapId, creationDate, hash, title, artist, lastUpdate, removed, published, removalReason);
	}
}
