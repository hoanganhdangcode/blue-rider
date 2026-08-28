package com.blueride.crypto.password;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

/**
 * Argon2id hasher (PHC string format). Do not log passwords or hashes in callers.
 */
public final class Argon2idPasswordHasher implements PasswordHasher {

    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;
    private static final int PARALLELISM = 1;
    private static final int MEMORY_KB = 16_384;
    private static final int ITERATIONS = 2;
    private static final String PREFIX = "$argon2id$v=19$m=" + MEMORY_KB + ",t=" + ITERATIONS + ",p=" + PARALLELISM + "$";

    private final SecureRandom random = new SecureRandom();

    @Override
    public String hash(char[] password) {
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        byte[] digest = digest(password, salt);
        return PREFIX + b64(salt) + "$" + b64(digest);
    }

    @Override
    public boolean matches(char[] password, String encoded) {
        if (encoded == null || !encoded.startsWith(PREFIX)) {
            return false;
        }
        String remainder = encoded.substring(PREFIX.length());
        int sep = remainder.indexOf('$');
        if (sep <= 0) {
            return false;
        }
        byte[] salt;
        byte[] expected;
        try {
            salt = Base64.getDecoder().decode(remainder.substring(0, sep));
            expected = Base64.getDecoder().decode(remainder.substring(sep + 1));
        } catch (IllegalArgumentException ex) {
            return false;
        }
        byte[] actual = digest(password, salt);
        return MessageDigest.isEqual(expected, actual);
    }

    private static byte[] digest(char[] password, byte[] salt) {
        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(PARALLELISM)
                .withMemoryAsKB(MEMORY_KB)
                .withIterations(ITERATIONS)
                .build();
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(params);
        byte[] output = new byte[HASH_LENGTH];
        generator.generateBytes(new String(password).getBytes(StandardCharsets.UTF_8), output);
        return output;
    }

    private static String b64(byte[] bytes) {
        return Base64.getEncoder().withoutPadding().encodeToString(bytes);
    }
}
