package com.blueride.crypto;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class EcdhAesRoundTripTest {

	@Test
	void sharedSecretEncryptsAndDecrypts() {
		KeyPair alice = EcdhSession.generate();
		KeyPair bob = EcdhSession.generate();
		byte[] aliceSecret = EcdhSession.sharedSecret(alice.getPrivate(), bob.getPublic());
		byte[] bobSecret = EcdhSession.sharedSecret(bob.getPrivate(), alice.getPublic());
		assertArrayEquals(aliceSecret, bobSecret);

		byte[] key = AesGcm.keyFromSharedSecret(aliceSecret);
		byte[] plaintext = "ping".getBytes(StandardCharsets.UTF_8);
		byte[] ciphertext = AesGcm.encrypt(key, plaintext);
		assertFalse(Arrays.equals(plaintext, ciphertext));
		assertArrayEquals(plaintext, AesGcm.decrypt(key, ciphertext));
	}
}
