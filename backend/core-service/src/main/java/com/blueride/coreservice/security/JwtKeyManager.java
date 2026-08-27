package com.blueride.coreservice.security;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads or bootstraps the RSA key pair used for JWT signing/verification.
 *
 * <p>Startup flow:
 * <ol>
 *   <li>Resolve private/public key paths from {@link JwtKeyProperties}.</li>
 *   <li>If <b>both</b> key files exist: load and validate them. Fail startup with a clear
 *       message if they are corrupted or not the same pair (never silently overwrite).</li>
 *   <li>If <b>any</b> of the two is missing: generate a fresh RSA pair, persist both PEM files
 *       (private with owner-only permission), then load and validate.</li>
 * </ol>
 *
 * <p>Calling {@link #initialize()} more than once on the same instance throws, preventing
 * accidental regeneration after a pair was already published.
 */
public class JwtKeyManager {

    private static final Logger log = LoggerFactory.getLogger(JwtKeyManager.class);

    private final JwtKeyProperties properties;
    private final RsaKeyGenerator generator;
    private final Path privateKeyPath;
    private final Path publicKeyPath;

    private volatile boolean initialized;
    private volatile KeyPairHolder holder;

    public JwtKeyManager(JwtKeyProperties properties, RsaKeyGenerator generator) {
        this.properties = properties;
        this.generator = generator;
        this.privateKeyPath = Path.of(properties.resolvedPrivateKey()).toAbsolutePath();
        this.publicKeyPath = Path.of(properties.resolvedPublicKey()).toAbsolutePath();
    }

    public synchronized void initialize() {
        if (initialized) {
            throw new IllegalStateException("JwtKeyManager.initialize() must only be called once");
        }
        log.info("Initializing JWT RSA keys from {}", privateKeyPath);
        KeyPair keyPair = loadOrGenerate();
        this.holder = new KeyPairHolder(keyPair);
        this.initialized = true;
        log.info("JWT RSA key pair ready (kid={})", holder.kid());
    }

    public KeyPairHolder keyPair() {
        ensureInitialized();
        return holder;
    }

    private KeyPair loadOrGenerate() {
        boolean privateExists = Files.isRegularFile(privateKeyPath);
        boolean publicExists = Files.isRegularFile(publicKeyPath);

        if (privateExists && publicExists) {
            log.info("Both key files exist, loading existing key pair");
            return loadAndValidate();
        }

        if (privateExists || publicExists) {
            String found = privateExists ? "private" : "";
            found = publicExists ? (found.isEmpty() ? "public" : found + " and public") : found;
            throw new IllegalStateException(
                    "Incomplete key pair: expected both files to exist at '"
                            + privateKeyPath + "' and '" + publicKeyPath + "'. Found "
                            + found + " only. Refusing to regenerate because the existing key"
                            + " would be lost. Restore the missing key file or move the partial"
                            + " files away to bootstrap a fresh pair.");
        }

        log.info("No key files found, generating a new RSA-{} key pair",
                properties.getRsaKeySize());
        generateAndSave();
        return loadAndValidate();
    }

    private KeyPair loadAndValidate() {
        PrivateKey privateKey = PemCodec.decodePrivateKey(PemCodec.readText(privateKeyPath));
        PublicKey publicKey = PemCodec.decodePublicKey(PemCodec.readText(publicKeyPath));

        KeyPair candidate = new KeyPair(publicKey, privateKey);
        KeyPairValidator.validate(candidate);
        return candidate;
    }

    private void generateAndSave() {
        KeyPair generated = generator.generate();
        try {
            PemCodec.write(privateKeyPath, PemCodec.encodePrivateKey(generated.getPrivate()), true);
            PemCodec.write(publicKeyPath, PemCodec.encodePublicKey(generated.getPublic()), false);
        } catch (RuntimeException e) {
            throw new IllegalStateException(
                    "Failed to write generated RSA keys to '" + privateKeyPath.getParent()
                            + "'. Ensure the directory is mounted and writable (e.g. a Docker "
                            + "volume at /app/keys).", e);
        }
    }

    private void ensureInitialized() {
        if (!initialized) {
            throw new IllegalStateException("JwtKeyManager has not been initialized");
        }
    }
}
