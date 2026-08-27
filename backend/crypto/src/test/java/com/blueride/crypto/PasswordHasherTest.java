package com.blueride.crypto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {

	private final PasswordHasher hasher = new PasswordHasher();

	@Test
	void hashThenVerifySucceeds() {
		char[] password = "s3cret-pass!".toCharArray();
		String encoded = hasher.hash(password);
		assertTrue(encoded.startsWith("$argon2id$"));
		assertTrue(hasher.verify("s3cret-pass!".toCharArray(), encoded));
	}

	@Test
	void wrongPasswordFails() {
		String encoded = hasher.hash("correct".toCharArray());
		assertFalse(hasher.verify("wrong".toCharArray(), encoded));
	}
}
