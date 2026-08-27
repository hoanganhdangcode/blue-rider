package com.blueride.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

import javax.crypto.KeyAgreement;

/**
 * ECDH P-256 key agreement. Not wired to HTTP traffic in the base scaffold.
 */
public final class EcdhSession {

	private EcdhSession() {
	}

	public static KeyPair generate() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
			generator.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
			return generator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to generate ECDH key pair", ex);
		}
	}

	public static byte[] sharedSecret(PrivateKey privateKey, PublicKey peerPublicKey) {
		try {
			KeyAgreement agreement = KeyAgreement.getInstance("ECDH");
			agreement.init(privateKey);
			agreement.doPhase(peerPublicKey, true);
			return agreement.generateSecret();
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to derive ECDH shared secret", ex);
		}
	}
}
