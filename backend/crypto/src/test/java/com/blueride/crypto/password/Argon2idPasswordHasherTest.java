package com.blueride.crypto.password;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Argon2idPasswordHasherTest {

    private final PasswordHasher hasher = new Argon2idPasswordHasher();

    @Test
    void hashThenMatches() {
        char[] password = "correct-horse-battery".toCharArray();
        String hash = hasher.hash(password);
        assertTrue(hasher.matches(password, hash));
        assertFalse(hasher.matches("wrong-password".toCharArray(), hash));
        assertNotEquals(new String(password), hash);
    }

    @Test
    void blankHashDoesNotMatch() {
        assertFalse(hasher.matches("secret".toCharArray(), ""));
        assertFalse(hasher.matches("secret".toCharArray(), null));
    }
}
