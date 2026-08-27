package com.blueride.crypto;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * JWT sign/verify with RSA-256. Not attached to login in the base scaffold.
 */
public final class JwtService {

	public static final String DEFAULT_KEY_ID = "blueride-rsa-1";
	public static final String DEFAULT_ISSUER = "blue-ride";

	private final RSAKey rsaKey;
	private final String issuer;

	public JwtService(KeyPair keyPair) {
		this(keyPair, DEFAULT_KEY_ID, DEFAULT_ISSUER);
	}

	public JwtService(KeyPair keyPair, String keyId, String issuer) {
		Objects.requireNonNull(keyPair, "keyPair");
		this.issuer = issuer == null || issuer.isBlank() ? DEFAULT_ISSUER : issuer;
		this.rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
				.privateKey((RSAPrivateKey) keyPair.getPrivate())
				.keyID(keyId == null || keyId.isBlank() ? DEFAULT_KEY_ID : keyId)
				.algorithm(JWSAlgorithm.RS256)
				.keyUse(KeyUse.SIGNATURE)
				.build();
	}

	public String sign(String subject, Duration ttl, Map<String, Object> extraClaims) {
		Objects.requireNonNull(subject, "subject");
		Objects.requireNonNull(ttl, "ttl");
		Instant now = Instant.now();
		JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
				.subject(subject)
				.issuer(issuer)
				.jwtID(UUID.randomUUID().toString())
				.issueTime(Date.from(now))
				.expirationTime(Date.from(now.plus(ttl)));
		if (extraClaims != null) {
			extraClaims.forEach(builder::claim);
		}
		try {
			SignedJWT jwt = new SignedJWT(
					new JWSHeader.Builder(JWSAlgorithm.RS256)
							.keyID(rsaKey.getKeyID())
							.type(JOSEObjectType.JWT)
							.build(),
					builder.build());
			jwt.sign(new RSASSASigner(rsaKey.toPrivateKey()));
			return jwt.serialize();
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to sign JWT", ex);
		}
	}

	public JWTClaimsSet verify(String token) {
		Objects.requireNonNull(token, "token");
		try {
			SignedJWT jwt = SignedJWT.parse(token);
			if (!jwt.verify(new RSASSAVerifier(rsaKey.toRSAPublicKey()))) {
				throw new IllegalArgumentException("Invalid JWT signature");
			}
			JWTClaimsSet claims = jwt.getJWTClaimsSet();
			Date expiration = claims.getExpirationTime();
			if (expiration == null || expiration.toInstant().isBefore(Instant.now())) {
				throw new IllegalArgumentException("JWT expired");
			}
			return claims;
		} catch (IllegalArgumentException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new IllegalArgumentException("Invalid JWT", ex);
		}
	}

	public String jwksJson() {
		try {
			return new JWKSet(rsaKey.toPublicJWK()).toString();
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to export JWKS", ex);
		}
	}

	public String keyId() {
		return rsaKey.getKeyID();
	}
}
