package com.blueride.crypto.jwt;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * RS256 JWT sign/verify. Identity will sign; other services verify with the public key.
 */
public final class RsaJwtService implements JwtSigner, JwtVerifier {

    private static final String ISSUER = "blue-ride";
    private static final Duration DEFAULT_TTL = Duration.ofHours(1);

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private final Duration ttl;

    public RsaJwtService(KeyPair keyPair) {
        this(keyPair, DEFAULT_TTL);
    }

    public RsaJwtService(KeyPair keyPair, Duration ttl) {
        Objects.requireNonNull(keyPair, "keyPair");
        if (!(keyPair.getPrivate() instanceof RSAPrivateKey rsaPrivate)
                || !(keyPair.getPublic() instanceof RSAPublicKey rsaPublic)) {
            throw new IllegalArgumentException("Key pair must be RSA");
        }
        this.privateKey = rsaPrivate;
        this.publicKey = rsaPublic;
        this.ttl = ttl;
    }

    @Override
    public String sign(Map<String, Object> claims) {
        Instant now = Instant.now();
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer(ISSUER)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(ttl)));
        if (claims != null) {
            claims.forEach(builder::claim);
        }
        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), builder.build());
        try {
            jwt.sign(new RSASSASigner(privateKey));
        } catch (JOSEException ex) {
            throw new IllegalStateException("Unable to sign JWT", ex);
        }
        return jwt.serialize();
    }

    @Override
    public Map<String, Object> verify(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            if (!jwt.verify(new RSASSAVerifier(publicKey))) {
                throw new IllegalArgumentException("Invalid JWT signature");
            }
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            Date expiry = claims.getExpirationTime();
            if (expiry != null && expiry.toInstant().isBefore(Instant.now())) {
                throw new IllegalArgumentException("JWT has expired");
            }
            return new LinkedHashMap<>(claims.getClaims());
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to verify JWT", ex);
        }
    }
}
