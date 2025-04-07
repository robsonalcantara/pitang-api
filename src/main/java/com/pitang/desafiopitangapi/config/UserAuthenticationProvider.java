package com.pitang.desafiopitangapi.config;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;


import com.pitang.desafiopitangapi.domain.dto.UserDTO;
import com.pitang.desafiopitangapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;

@Configuration
public class UserAuthenticationProvider {

	@Value("${api.security.token.secret}")
	private String secretKey;

	@Value("${api.security.token.expiration}")
	private String expiration;

	@Autowired
	private UserService userService;

	@PostConstruct
	protected void init() {
		// this is to avoid having the raw secret key available in the JVM
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(UserDTO user) {
		Date now = new Date();
		int expirationToken = 3600000; // 1 hour

		try {
			expirationToken = Integer.parseInt(expiration);
		} catch (NumberFormatException e) {
		}

		Date validity = new Date(now.getTime() + expirationToken);

		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		return JWT.create().withSubject(user.getLogin()).withIssuedAt(now).withExpiresAt(validity)
				.withClaim("id", user.getId().toString()).withClaim("firstName", user.getFirstName())
				.withClaim("lastName", user.getLastName()).sign(algorithm);
	}

	public Authentication validateToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);

		JWTVerifier verifier = JWT.require(algorithm).build();

		DecodedJWT decoded = verifier.verify(token);

		UserDTO user = new UserDTO(decoded.getClaim("id").asString(), decoded.getSubject());

		return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
	}

	public Authentication validateTokenStrongly(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);

		JWTVerifier verifier = JWT.require(algorithm).build();

		DecodedJWT decoded = verifier.verify(token);

		return new UsernamePasswordAuthenticationToken(userService.findByLogin(decoded.getSubject()), null,
				Collections.emptyList());
	}
}
