package com.blueride.gateway.security;

import java.util.Optional;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class NoOpJwtAuthenticator implements JwtAuthenticator {

	@Override
	public Optional<AuthPrincipal> authenticate(HttpServletRequest request) {
		return Optional.empty();
	}
}
