package com.adosar.backend.business.service;

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
final public class JWTService {

	public static String createJWT(Integer userId) {
		Algorithm algorithm = Algorithm.HMAC512(System.getenv("HMAC512_SECRET"));
		return JWT.create()
				.withIssuer("adosar")
				.withSubject("user auth")
				.withClaim("userId", userId)
				.withIssuedAt(Instant.now())
				.withExpiresAt(Instant.now().plusSeconds(604800))
				.withJWTId(UUID.randomUUID().toString())
				.withNotBefore(Instant.now())
				.sign(algorithm);
	}

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
