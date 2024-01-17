package com.adosar.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public final class Authorization {
	public final User user;
	public final Boolean isAuthorized;

	@Override
	public String toString() {
		return String.format("(user=%s, isAuthorized=%s)", user, isAuthorized);
	}
}
