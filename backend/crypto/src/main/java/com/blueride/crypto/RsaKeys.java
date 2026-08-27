package com.blueride.crypto;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA-2048 key generation and PKCS#8 / X.509 PEM load/store.
 */
public final class RsaKeys {

	private static final int KEY_SIZE = 2048;
	private static final String BEGIN_PRIVATE = "-----BEGIN PRIVATE KEY-----";
	private static final String END_PRIVATE = "-----END PRIVATE KEY-----";
	private static final String BEGIN_PUBLIC = "-----BEGIN PUBLIC KEY-----";
	private static final String END_PUBLIC = "-----END PUBLIC KEY-----";

	private RsaKeys() {
	}

	public static KeyPair generate() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(KEY_SIZE);
			return generator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to generate RSA key pair", ex);
		}
	}

	public static KeyPair fromPem(String privatePem, String publicPem) {
		return new KeyPair(publicKeyFromPem(publicPem), privateKeyFromPem(privatePem));
	}

	public static String privatePem(PrivateKey key) {
		return wrap(BEGIN_PRIVATE, END_PRIVATE, key.getEncoded());
	}

	public static String publicPem(PublicKey key) {
		return wrap(BEGIN_PUBLIC, END_PUBLIC, key.getEncoded());
	}

	public static PrivateKey privateKeyFromPem(String pem) {
		try {
			byte[] der = unwrap(normalizePem(pem), BEGIN_PRIVATE, END_PRIVATE);
			return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(der));
		} catch (Exception ex) {
			throw new IllegalArgumentException("Invalid RSA private key PEM", ex);
		}
	}

	public static PublicKey publicKeyFromPem(String pem) {
		try {
			byte[] der = unwrap(normalizePem(pem), BEGIN_PUBLIC, END_PUBLIC);
			return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(der));
		} catch (Exception ex) {
			throw new IllegalArgumentException("Invalid RSA public key PEM", ex);
		}
	}

	private static String normalizePem(String pem) {
		if (pem == null || pem.isBlank()) {
			throw new IllegalArgumentException("PEM must not be blank");
		}
		return pem.replace("\\n", "\n").trim();
	}

	private static String wrap(String begin, String end, byte[] der) {
		String body = Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(der);
		return begin + "\n" + body + "\n" + end + "\n";
	}

	private static byte[] unwrap(String pem, String begin, String end) {
		String body = pem.replace(begin, "").replace(end, "").replaceAll("\\s", "");
		return Base64.getDecoder().decode(body);
	}
}
