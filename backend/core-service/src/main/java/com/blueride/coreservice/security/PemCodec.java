package com.blueride.coreservice.security;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermission;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Set;

/**
 * Encodes/decodes RSA keys to/from PEM. The decode methods validate and fail on
 * corrupted input. Saving is atomic (write temp + move) and restricts POSIX
 * permission to owner-only read/write for the private key.
 */
public final class PemCodec {

    private static final String PRIVATE_BEGIN = "-----BEGIN PRIVATE KEY-----";
    private static final String PRIVATE_END = "-----END PRIVATE KEY-----";
    private static final String PUBLIC_BEGIN = "-----BEGIN PUBLIC KEY-----";
    private static final String PUBLIC_END = "-----END PUBLIC KEY-----";

    private PemCodec() {
    }

    public static String encodePrivateKey(PrivateKey key) {
        return toPem(PRIVATE_BEGIN, PRIVATE_END, key.getEncoded());
    }

    public static String encodePublicKey(PublicKey key) {
        return toPem(PUBLIC_BEGIN, PUBLIC_END, key.getEncoded());
    }

    public static PrivateKey decodePrivateKey(String pem) {
        byte[] der = decodeBody(pem, PRIVATE_BEGIN, PRIVATE_END);
        try {
            return KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(der));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid or corrupted RSA private key", e);
        }
    }

    public static PublicKey decodePublicKey(String pem) {
        byte[] der = decodeBody(pem, PUBLIC_BEGIN, PUBLIC_END);
        try {
            return KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(der));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid or corrupted RSA public key", e);
        }
    }

    public static String readText(Path path) {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read key file: " + path, e);
        }
    }

    /** Writes content atomically with owner-only permissions. */
    public static void write(Path path, String content, boolean restrictPermission) {
        try {
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Path tmp = Files.createTempFile(parent, path.getFileName().toString(), ".tmp");
            try {
                Files.writeString(tmp, content, StandardCharsets.UTF_8,
                        StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                if (restrictPermission) {
                    setOwnerOnlyPermissions(tmp);
                }
                Files.move(tmp, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                        java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            } finally {
                Files.deleteIfExists(tmp);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write key file: " + path, e);
        }
    }

    private static void setOwnerOnlyPermissions(Path path) throws IOException {
        if (path.getFileSystem().supportedFileAttributeViews().contains("posix")) {
            Set<PosixFilePermission> perms = Set.of(
                    PosixFilePermission.OWNER_READ,
                    PosixFilePermission.OWNER_WRITE);
            Files.setPosixFilePermissions(path, perms);
        }
    }

    private static byte[] decodeBody(String pem, String begin, String end) {
        if (pem == null) {
            throw new IllegalArgumentException("PEM content must not be null");
        }
        String cleaned = pem.replace("\r", "");
        int start = cleaned.indexOf(begin);
        int stop = cleaned.indexOf(end);
        if (start < 0 || stop < 0 || stop <= start) {
            throw new IllegalArgumentException("Missing PEM markers; expected " + begin + " ... " + end);
        }
        String body = cleaned.substring(start + begin.length(), stop);
        String compact = body.replace("\n", "").replace(" ", "");
        try {
            return Base64.getDecoder().decode(compact);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Corrupted base64 in PEM body", e);
        }
    }

    private static String toPem(String begin, String end, byte[] der) {
        String base64 = Base64.getEncoder().encodeToString(der);
        StringBuilder sb = new StringBuilder(begin).append('\n');
        for (int i = 0; i < base64.length(); i += 64) {
            sb.append(base64, i, Math.min(i + 64, base64.length())).append('\n');
        }
        return sb.append(end).append('\n').toString();
    }
}
