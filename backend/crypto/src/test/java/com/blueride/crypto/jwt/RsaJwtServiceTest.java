package com.blueride.crypto.jwt;

import java.security.KeyPair;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RsaJwtServiceTest {

    @Test
    void signThenVerifyRoundTrip() {
        KeyPair keys = RsaKeyPairs.ephemeral();
        RsaJwtService jwt = new RsaJwtService(keys);
        String token = jwt.sign(Map.of("sub", "user-1", "role", "RIDER"));
        Map<String, Object> claims = jwt.verify(token);
        assertEquals("user-1", claims.get("sub"));
        assertEquals("RIDER", claims.get("role"));
        assertEquals("blue-ride", claims.get("iss"));
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void verifyRejectsTamperedToken() {
        RsaJwtService jwt = new RsaJwtService(RsaKeyPairs.ephemeral());
        String token = jwt.sign(Map.of("sub", "user-1"));
        String tampered = token.substring(0, token.length() - 2) + "xx";
        assertThrows(IllegalArgumentException.class, () -> jwt.verify(tampered));
    }

    @Test
    void loadOrEphemeralUsesGeneratedKeysWhenPemBlank() {
        KeyPair keys = RsaKeyPairs.loadOrEphemeral("", "");
        RsaJwtService jwt = new RsaJwtService(keys);
        assertEquals("user-1", jwt.verify(jwt.sign(Map.of("sub", "user-1"))).get("sub"));
    }
}
