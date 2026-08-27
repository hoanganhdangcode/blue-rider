package com.blueride.gateway.security;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Placeholder JWT authenticator. Base scaffold does not require a token.
 */
public interface JwtAuthenticator {

	Optional<AuthPrincipal> authenticate(HttpServletRequest request);
}
