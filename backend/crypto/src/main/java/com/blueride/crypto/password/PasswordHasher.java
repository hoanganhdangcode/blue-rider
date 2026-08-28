package com.blueride.crypto.password;

/**
 * One-way password hashing. Implementations must use a slow KDF (Argon2id).
 */
public interface PasswordHasher {

    String hash(char[] password);

    boolean matches(char[] password, String hash);
}
