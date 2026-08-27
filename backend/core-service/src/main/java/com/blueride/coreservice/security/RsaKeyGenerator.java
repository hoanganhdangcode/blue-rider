package com.blueride.coreservice.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Generates RSA key pairs. Contains no filesystem side effects so it is easy to
 * unit test and to reuse for generating throwaway keys in tests.
 */
public class RsaKeyGenerator {

    private final int keySize;

    public RsaKeyGenerator(int keySize) {
        if (keySize < 2048) {
            throw new IllegalArgumentException(
                    "RSA key size must be at least 2048 bits, got " + keySize);
        }
        this.keySize = keySize;
    }

    public KeyPair generate() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(keySize, new SecureRandom());
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA algorithm not available", e);
        }
    }
}
