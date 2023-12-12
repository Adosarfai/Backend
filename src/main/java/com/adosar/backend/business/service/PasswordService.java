package com.adosar.backend.business.service;

import com.password4j.Hash;
import com.password4j.Password;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public final class PasswordService {

	/**
	 * Hashes a string using argon2
	 *
	 * @param password password
	 * @return Argon2 hash of password
	 */
	public static Hash hashPassword(String password) {
		return Password.hash(password)
				.addRandomSalt(32)
				.withArgon2();
	}
}
