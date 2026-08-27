package com.blueride.crypto.jwt;

import java.io.StringReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

/**
 * RSA-2048 key loading. Blank PEMs yield an ephemeral in-memory pair (local/dev only).
 * Never log PEM material.
 */
public final class RsaKeyPairs {

    private static final int RSA_BITS = 2048;

    private RsaKeyPairs() {
    }

    public static KeyPair loadOrEphemeral(String privatePem, String publicPem) {
        if (privatePem == null || privatePem.isBlank()) {
            return ephemeral();
        }
        return fromPem(privatePem, publicPem);
    }

    public static KeyPair ephemeral() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(RSA_BITS);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("RSA key generation is not available", ex);
        }
    }

    public static KeyPair fromPem(String privatePem, String publicPem) {
        PrivateKey privateKey = readPrivateKey(privatePem);
        PublicKey publicKey = publicPem == null || publicPem.isBlank()
                ? derivePublic(privateKey)
                : readPublicKey(publicPem);
        return new KeyPair(publicKey, privateKey);
    }

    private static PublicKey derivePublic(PrivateKey privateKey) {
        if (privateKey instanceof RSAPrivateCrtKey crt) {
            try {
                RSAPublicKeySpec spec = new RSAPublicKeySpec(crt.getModulus(), crt.getPublicExponent());
                return KeyFactory.getInstance("RSA").generatePublic(spec);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Unable to derive RSA public key", ex);
            }
        }
        throw new IllegalArgumentException("Private key is not an RSA CRT key; provide a public PEM");
    }

    private static PrivateKey readPrivateKey(String pem) {
        try (PEMParser parser = new PEMParser(new StringReader(pem))) {
            Object object = parser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            if (object instanceof PEMKeyPair keyPair) {
                return converter.getPrivateKey(keyPair.getPrivateKeyInfo());
            }
            if (object instanceof PrivateKeyInfo info) {
                return converter.getPrivateKey(info);
            }
            throw new IllegalArgumentException("Unsupported private key PEM");
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to parse private key PEM", ex);
        }
    }

    private static PublicKey readPublicKey(String pem) {
        try (PEMParser parser = new PEMParser(new StringReader(pem))) {
            Object object = parser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            if (object instanceof SubjectPublicKeyInfo info) {
                return converter.getPublicKey(info);
            }
            throw new IllegalArgumentException("Unsupported public key PEM");
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to parse public key PEM", ex);
        }
    }
}
