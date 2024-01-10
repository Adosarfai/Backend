package com.adosar.backend.business.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

	// createJWT returns a non-null string with valid input
	@Test
	void test_createJWT_returns_a_non_null_string_with_valid_input() {
		// Arrange
		Integer userId = 123;

		// Act
		String jwt = JWTService.createJWT(userId);

		// Assert
		assertNotNull(jwt);
	}

	// JWT token has an unique ID
	@Test
	void test_jwt_token_has_an_unique_id() {
		// Arrange
		Integer userId = 123;

		// Act
		String jwt = JWTService.createJWT(userId);
		DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);

		// Assert
		assertNotNull(decodedJWT);
		assertNotNull(decodedJWT.getId());
	}

	// JWT token has expected expiration time
	@Test
	void test_jwt_token_has_expected_expiration_time() {
		// Arrange
		Integer userId = 123;

		// Act
		String jwt = JWTService.createJWT(userId);
		DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);
		assert decodedJWT != null;
		Instant expirationTime = decodedJWT.getExpiresAt().toInstant().truncatedTo(ChronoUnit.SECONDS);
		Instant expectedExpirationTime = Instant.now().plusSeconds(604800).truncatedTo(ChronoUnit.SECONDS);

		// Assert
		assertNotNull(decodedJWT);
		assertEquals(expectedExpirationTime, expirationTime);
	}

	// JWT token contains expected claims and values
	@Test
	void test_jwt_token_contains_expected_claims_and_values() {
		// Arrange
		Integer userId = 123;

		// Act
		String jwt = JWTService.createJWT(userId);
		DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);

		// Assert
		assertNotNull(decodedJWT);
		assertEquals("adosar", decodedJWT.getIssuer());
		assertEquals("user auth", decodedJWT.getSubject());
		assertEquals(userId, decodedJWT.getClaim("userId").asInt());
	}

	// verifyJWT returns a non-null DecodedJWT object with valid input
	@Test
	void test_verifyJWT_returns_a_non_null_DecodedJWT_object_with_valid_input() {
		// Arrange
		Integer userId = 123;

		// Act
		String jwt = JWTService.createJWT(userId);
		DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);

		// Assert
		assertNotNull(decodedJWT);
	}

	// JWT token is signed with expected algorithm, secret, issuer and subject
	@Test
	void test_jwt_token_is_signed_with_expected_algorithm_secret_issuer_and_subject() {
		// Arrange
		Integer userId = 123;
		String expectedIssuer = "adosar";
		String expectedSubject = "user auth";
		Algorithm expectedAlgorithm = Algorithm.HMAC512(System.getenv("HMAC512_SECRET"));

		// Act
		String jwtToken = JWTService.createJWT(userId);
		DecodedJWT decodedJWT = JWTService.verifyJWT(jwtToken);

		// Assert
		assertNotNull(decodedJWT);
		assertEquals(expectedIssuer, decodedJWT.getIssuer());
		assertEquals(expectedSubject, decodedJWT.getSubject());
		assertEquals(userId, decodedJWT.getClaim("userId").asInt());
		assertTrue(decodedJWT.getIssuedAt().before(new Date()));
		assertTrue(decodedJWT.getExpiresAt().after(new Date()));
		assertNotNull(decodedJWT.getId());
		assertTrue(decodedJWT.getNotBefore().before(new Date()));
		assertDoesNotThrow(() -> expectedAlgorithm.verify(decodedJWT));
	}

	// verifyJWT returns null with invalid input
	@Test
	void test_verifyJWT_returns_null_with_invalid_input() {
		// Arrange
		String invalidJWT = "invalid_token";

		// Act
		DecodedJWT result = JWTService.verifyJWT(invalidJWT);

		// Assert
		assertNull(result);
	}

	// createJWT throws exception with null input
	@Test
	void test_createJWTThrowsExceptionWithNullInput() {
		// Assert
		assertThrows(NullPointerException.class, () -> {
			JWTService.createJWT(null);
		});
	}

	// JWT token has expected not-before time
	@Test
	void test_jwt_token_has_expected_not_before_time() {
		// Arrange
		String jwt = JWTService.createJWT(123);

		// Act
		DecodedJWT decodedJWT = JWTService.verifyJWT(jwt);

		// Assert
		assertNotNull(decodedJWT);
		Instant notBefore = decodedJWT.getNotBefore().toInstant();
		Instant now = Instant.now();
		assertTrue(notBefore.isBefore(now) || notBefore.equals(now));
	}
}
