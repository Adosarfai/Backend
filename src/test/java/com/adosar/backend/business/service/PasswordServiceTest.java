package com.adosar.backend.business.service;

import com.password4j.Hash;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("UnusedShould")
class PasswordServiceTest {
	// Can hash a password successfully
	@Test
	void test_hash_password_successfully() {
		// Arrange
		String password = "password123";

		// Act
		Hash hash = PasswordService.hashPassword(password);

		// Assert
		assertNotNull(hash);
	}

	// Hashed password is not the same as original password
	@Test
	void test_hashed_password_not_same_as_original_password() {
		// Arrange
		String password = "password123";

		// Act
		Hash hashedPassword = PasswordService.hashPassword(password);

		// Assert
		assertNotEquals(password, hashedPassword.getResult());
	}

	// Hashed password is not consistent for the same input
	@Test
	void test_hashed_password_consistency() {
		// Arrange
		String password = "password123";

		// Act
		Hash hashedPassword1 = PasswordService.hashPassword(password);
		Hash hashedPassword2 = PasswordService.hashPassword(password);

		// Assert
		assertNotEquals(hashedPassword1, hashedPassword2);
	}

	// Hashed password is different for different input
	@Test
	void test_hashed_password_is_different_for_different_input() {
		// Arrange
		String password1 = "password1";
		String password2 = "password2";

		// Act
		Hash hashedPassword1 = PasswordService.hashPassword(password1);
		Hash hashedPassword2 = PasswordService.hashPassword(password2);

		// Assert
		assertNotEquals(hashedPassword1, hashedPassword2);
	}

	// Can hash passwords of different lengths
	@Test
	void test_hash_passwords_of_different_lengths() {
		// Arrange
		String password1 = "password";
		String password2 = "passwordbutlonger";
		String password3 = "passwordbutitsactuallyverylong";

		// Act
		Hash hash1 = PasswordService.hashPassword(password1);
		Hash hash2 = PasswordService.hashPassword(password2);
		Hash hash3 = PasswordService.hashPassword(password3);

		// Assert
		assertNotNull(hash1);
		assertNotNull(hash2);
		assertNotNull(hash3);
	}

	// Can hash passwords with special characters
	@Test
	void test_hash_password_with_special_characters() {
		// Arrange
		String password = "P@ssw0rd!";

		// Act
		Hash hashedPassword = PasswordService.hashPassword(password);

		// Assert
		assertNotNull(hashedPassword);
	}

	// Can hash passwords with numbers
	@Test
	void test_hash_password_with_numbers() {
		// Arrange
		String password = "password123";

		// Act
		Hash hash = PasswordService.hashPassword(password);

		// Assert
		assertNotNull(hash);
	}

	// Can hash passwords with uppercase and lowercase letters
	@Test
	void test_hash_password_with_uppercase_and_lowercase_letters() {
		// Arrange
		String password = "Password123";

		// Act
		Hash hash = PasswordService.hashPassword(password);

		// Assert
		assertNotNull(hash);
	}

	// Can hash passwords with non-ASCII characters
	@Test
	void test_hash_password_with_non_ascii_characters() {
		// Arrange
		String password = "パスワード"; // non-ASCII password

		// Act
		Hash hash = PasswordService.hashPassword(password);

		// Assert
		assertNotNull(hash);
	}

}