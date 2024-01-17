package com.adosar.backend.business.service;

import com.adosar.backend.domain.Privilege;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@NoArgsConstructor
public final class JWTService {


	/**
	 * Creates a JWT token for a user
	 *
	 * @param userId user ID
	 * @return JWT token
	 */
	public static String createJWT(Integer userId, Privilege privilege) {
		if (userId == null) throw new NullPointerException();

		Algorithm algorithm = Algorithm.HMAC512(System.getenv("HMAC512_SECRET"));
		return JWT.create()
				.withIssuer("adosar")
				.withSubject("user auth")
				.withClaim("userId", userId)
				.withClaim("privilege", privilege.toString())
				.withIssuedAt(Instant.now())
				.withExpiresAt(Instant.now().plusSeconds(604800))
				.withJWTId(UUID.randomUUID().toString())
				.withNotBefore(Instant.now())
				.sign(algorithm);
	}

	/**
	 * @param jwt JWT token
	 * @return Decoded JWT object
	 */
	public static DecodedJWT verifyJWT(String jwt) {
		try {

			Algorithm algorithm = Algorithm.HMAC512(System.getenv("HMAC512_SECRET"));
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer("adosar")
					.withSubject("user auth")
					.build();
			return verifier.verify(jwt);
		} catch (Exception exception) {
			return null;
		}
	}
}
