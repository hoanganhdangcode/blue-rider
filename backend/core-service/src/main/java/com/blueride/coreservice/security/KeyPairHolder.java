package com.blueride.coreservice.security;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Immutable holder for the validated RSA key pair. Only published after
 * {@link JwtKeyManager} has successfully loaded/generated and validated the pair,
 * so consumers never observe an inconsistent (unvalidated) key state.
 */
public final class KeyPairHolder {

    private final KeyPair keyPair;
    private final String kid;

    public KeyPairHolder(KeyPair keyPair) {
        this(keyPair, "blue-rider-rsa-key-001");
    }

    public KeyPairHolder(KeyPair keyPair, String kid) {
        this.keyPair = keyPair;
        this.kid = kid;
    }

    public KeyPair keyPair() {
        return keyPair;
    }

    public PrivateKey privateKey() {
        return keyPair.getPrivate();
    }

    public PublicKey publicKey() {
        return keyPair.getPublic();
    }

    public String kid() {
        return kid;
    }
}
