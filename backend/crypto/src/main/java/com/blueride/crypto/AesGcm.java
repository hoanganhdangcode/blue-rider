package com.blueride.crypto;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES-256-GCM. Ciphertext format: 12-byte IV + ciphertext+tag. Not wired to HTTP in the base scaffold.
 */
public final class AesGcm {

	private static final int GCM_IV_LENGTH = 12;
	private static final int GCM_TAG_BITS = 128;
	private static final int AES_KEY_BITS = 256;

	private AesGcm() {
	}

	public static byte[] keyFromSharedSecret(byte[] sharedSecret) {
		try {
			byte[] digest = MessageDigest.getInstance("SHA-256").digest(sharedSecret);
			return Arrays.copyOf(digest, AES_KEY_BITS / 8);
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to derive AES key", ex);
		}
	}

	public static byte[] encrypt(byte[] key, byte[] plaintext) {
		try {
			byte[] iv = new byte[GCM_IV_LENGTH];
			new SecureRandom().nextBytes(iv);
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(GCM_TAG_BITS, iv));
			byte[] encrypted = cipher.doFinal(plaintext);
			byte[] out = new byte[iv.length + encrypted.length];
			System.arraycopy(iv, 0, out, 0, iv.length);
			System.arraycopy(encrypted, 0, out, iv.length, encrypted.length);
			return out;
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to encrypt with AES-GCM", ex);
		}
	}

	public static byte[] decrypt(byte[] key, byte[] ciphertext) {
		if (ciphertext.length <= GCM_IV_LENGTH) {
			throw new IllegalArgumentException("Ciphertext too short");
		}
		try {
			byte[] iv = Arrays.copyOfRange(ciphertext, 0, GCM_IV_LENGTH);
			byte[] encrypted = Arrays.copyOfRange(ciphertext, GCM_IV_LENGTH, ciphertext.length);
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(GCM_TAG_BITS, iv));
			return cipher.doFinal(encrypted);
		} catch (IllegalArgumentException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new IllegalArgumentException("Unable to decrypt with AES-GCM", ex);
		}
	}
}
