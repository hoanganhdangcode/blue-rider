package com.blueride.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.time.Duration;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.nimbusds.jwt.JWTClaimsSet;

class JwtServiceTest {

	@Test
	void signAndVerifyRoundTrip() throws Exception {
		KeyPair pair = RsaKeys.generate();
		JwtService jwt = new JwtService(pair);
		String token = jwt.sign("user-1", Duration.ofMinutes(5), Map.of("role", "RIDER"));
		JWTClaimsSet claims = jwt.verify(token);
		assertEquals("user-1", claims.getSubject());
		assertEquals("RIDER", claims.getStringClaim("role"));
		assertTrue(jwt.jwksJson().contains(jwt.keyId()));
	}

	@Test
	void pemRoundTripKeepsVerifyWorking() {
		KeyPair generated = RsaKeys.generate();
		KeyPair loaded = RsaKeys.fromPem(RsaKeys.privatePem(generated.getPrivate()), RsaKeys.publicPem(generated.getPublic()));
		JwtService jwt = new JwtService(loaded);
		String token = jwt.sign("user-2", Duration.ofMinutes(1), Map.of());
		assertEquals("user-2", jwt.verify(token).getSubject());
	}

	@Test
	void expiredTokenIsRejected() {
		JwtService jwt = new JwtService(RsaKeys.generate());
		String token = jwt.sign("user-3", Duration.ofSeconds(-1), Map.of());
		assertThrows(IllegalArgumentException.class, () -> jwt.verify(token));
	}
}
