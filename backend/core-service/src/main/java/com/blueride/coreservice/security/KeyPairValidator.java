package com.blueride.coreservice.security;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Arrays;

/**
 * Verifies that a {@link PublicKey} and {@link PrivateKey} really belong to the
 * same RSA key pair, using a cryptographic signature round-trip.
 */
public final class KeyPairValidator {

    public static final byte[] TEST_MESSAGE =
            "blueride-jwt-keypair-verification".getBytes(java.nio.charset.StandardCharsets.UTF_8);

    private KeyPairValidator() {
    }

    public static void validate(KeyPair keyPair) {
        if (keyPair == null || keyPair.getPrivate() == null || keyPair.getPublic() == null) {
            throw new IllegalArgumentException("Key pair must contain both public and private keys");
        }
        try {
            Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initSign(keyPair.getPrivate());
            signer.update(TEST_MESSAGE);
            byte[] signature = signer.sign();

            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(keyPair.getPublic());
            verifier.update(TEST_MESSAGE);
            if (!verifier.verify(signature)) {
                throw new IllegalArgumentException(
                        "Public and private keys do not belong to the same RSA key pair");
            }
        } catch (java.security.NoSuchAlgorithmException | java.security.InvalidKeyException
                 | java.security.SignatureException e) {
            throw new IllegalArgumentException("Failed to validate RSA key pair", e);
        }
    }

    public static boolean sameModulus(KeyPair a, KeyPair b) {
        try {
            java.security.interfaces.RSAPublicKey pubA =
                    (java.security.interfaces.RSAPublicKey) a.getPublic();
            java.security.interfaces.RSAPublicKey pubB =
                    (java.security.interfaces.RSAPublicKey) b.getPublic();
            return Arrays.equals(pubA.getModulus().toByteArray(), pubB.getModulus().toByteArray());
        } catch (ClassCastException e) {
            return false;
        }
    }
}
