package com.blueride.crypto;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

/**
 * Argon2id password hashing (PHC encoded). Not wired to login in the base scaffold.
 */
public final class PasswordHasher {

	private static final int SALT_LENGTH = 16;
	private static final int HASH_LENGTH = 32;
	private static final int MEMORY_KIB = 65_536;
	private static final int ITERATIONS = 3;
	private static final int PARALLELISM = 1;

	private final SecureRandom random = new SecureRandom();

	public String hash(char[] password) {
		Objects.requireNonNull(password, "password");
		byte[] salt = new byte[SALT_LENGTH];
		random.nextBytes(salt);
		byte[] hash = digest(password, salt, MEMORY_KIB, ITERATIONS, PARALLELISM);
		return encode(salt, hash, MEMORY_KIB, ITERATIONS, PARALLELISM);
	}

	public boolean verify(char[] password, String encodedHash) {
		Objects.requireNonNull(password, "password");
		Objects.requireNonNull(encodedHash, "encodedHash");
		ParsedHash parsed = parse(encodedHash);
		byte[] actual = digest(password, parsed.salt, parsed.memoryKib, parsed.iterations, parsed.parallelism);
		return constantTimeEquals(parsed.hash, actual);
	}

	private static byte[] digest(char[] password, byte[] salt, int memoryKib, int iterations, int parallelism) {
		byte[] passwordBytes = charsToBytes(password);
		try {
			Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
					.withVersion(Argon2Parameters.ARGON2_VERSION_13)
					.withMemoryAsKB(memoryKib)
					.withIterations(iterations)
					.withParallelism(parallelism)
					.withSalt(salt)
					.build();
			Argon2BytesGenerator generator = new Argon2BytesGenerator();
			generator.init(params);
			byte[] out = new byte[HASH_LENGTH];
			generator.generateBytes(passwordBytes, out);
			return out;
		} finally {
			Arrays.fill(passwordBytes, (byte) 0);
		}
	}

	private static String encode(byte[] salt, byte[] hash, int memoryKib, int iterations, int parallelism) {
		return "$argon2id$v=19$m=" + memoryKib + ",t=" + iterations + ",p=" + parallelism
				+ "$" + b64(salt) + "$" + b64(hash);
	}

	private static ParsedHash parse(String encoded) {
		String[] parts = encoded.split("\\$");
		if (parts.length != 6 || !"argon2id".equals(parts[1])) {
			throw new IllegalArgumentException("Unsupported password hash format");
		}
		if (!"v=19".equals(parts[2])) {
			throw new IllegalArgumentException("Unsupported Argon2 version");
		}
		int memoryKib = 0;
		int iterations = 0;
		int parallelism = 0;
		for (String token : parts[3].split(",")) {
			String[] kv = token.split("=", 2);
			if (kv.length != 2) {
				continue;
			}
			int value = Integer.parseInt(kv[1]);
			switch (kv[0].toLowerCase(Locale.ROOT)) {
				case "m" -> memoryKib = value;
				case "t" -> iterations = value;
				case "p" -> parallelism = value;
				default -> {
					// ignore unknown parameter
				}
			}
		}
		return new ParsedHash(b64d(parts[4]), b64d(parts[5]), memoryKib, iterations, parallelism);
	}

	private static String b64(byte[] data) {
		return Base64.getEncoder().withoutPadding().encodeToString(data);
	}

	private static byte[] b64d(String data) {
		return Base64.getDecoder().decode(data);
	}

	private static byte[] charsToBytes(char[] password) {
		return new String(password).getBytes(StandardCharsets.UTF_8);
	}

	private static boolean constantTimeEquals(byte[] a, byte[] b) {
		if (a.length != b.length) {
			return false;
		}
		int result = 0;
		for (int i = 0; i < a.length; i++) {
			result |= a[i] ^ b[i];
		}
		return result == 0;
	}

	private record ParsedHash(byte[] salt, byte[] hash, int memoryKib, int iterations, int parallelism) {
	}
}
