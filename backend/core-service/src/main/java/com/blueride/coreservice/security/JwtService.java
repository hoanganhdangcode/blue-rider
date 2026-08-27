package com.blueride.coreservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * Thin wrapper around JJWT for signing and verifying tokens with the managed RSA
 * key pair. Keys are fetched from {@link JwtKeyManager} lazily so this stays valid
 * across a key rotation simply by re-initializing the manager.
 */
public class JwtService {

    public static final String ISSUER = "blue-rider-core";
    public static final long DEFAULT_EXPIRES_IN_SECONDS = 3600;
    private static final io.jsonwebtoken.security.SignatureAlgorithm RS256 = Jwts.SIG.RS256;

    private final JwtKeyManager keyManager;

    public JwtService(JwtKeyManager keyManager) {
        this.keyManager = keyManager;
    }

    public String sign(String subject, Map<String, Object> claims) {
        return sign(subject, claims, DEFAULT_EXPIRES_IN_SECONDS);
    }

    public String sign(String subject, Map<String, Object> claims, long expiresInSeconds) {
        Instant now = Instant.now();
        PrivateKey privateKey = keyManager.keyPair().privateKey();
        return Jwts.builder()
                .subject(subject)
                .issuer(ISSUER)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expiresInSeconds)))
                .claims(claims)
                .header().keyId(keyManager.keyPair().kid()).and()
                .signWith(privateKey, RS256)
                .compact();
    }

    public Claims verify(String token) {
        PublicKey publicKey = keyManager.keyPair().publicKey();
        return Jwts.parser()
                .verifyWith(publicKey)
                .requireIssuer(ISSUER)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String kid() {
        return keyManager.keyPair().kid();
    }
}
