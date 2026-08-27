package com.blueride.coreservice.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JwtKeyManagerTest {

    @TempDir
    Path tempDir;

    private JwtKeyManager newManager(Path dir) {
        JwtKeyProperties properties = new JwtKeyProperties();
        properties.setDirectory(dir.toString());
        return new JwtKeyManager(properties, new RsaKeyGenerator(2048));
    }

    private Path privateKeyPath(Path dir) {
        return dir.resolve("private.key");
    }

    private Path publicKeyPath(Path dir) {
        return dir.resolve("public.key");
    }

    @Test
    void noKeys_generatesAndPersistsPair() throws Exception {
        JwtKeyManager manager = newManager(tempDir);

        manager.initialize();

        assertThat(privateKeyPath(tempDir)).isRegularFile();
        assertThat(publicKeyPath(tempDir)).isRegularFile();
        String privatePem = Files.readString(privateKeyPath(tempDir));
        assertThat(privatePem).contains("BEGIN PRIVATE KEY");
        String publicPem = Files.readString(publicKeyPath(tempDir));
        assertThat(publicPem).contains("BEGIN PUBLIC KEY");
        assertThat(manager.keyPair().keyPair()).isNotNull();
    }

    @Test
    void bothKeysExist_loadsWithoutGenerating() throws Exception {
        JwtKeyManager first = newManager(tempDir);
        first.initialize();
        byte[] expectedPrivate = Files.readAllBytes(privateKeyPath(tempDir));
        byte[] expectedPublic = Files.readAllBytes(publicKeyPath(tempDir));

        JwtKeyManager second = newManager(tempDir);
        second.initialize();

        assertThat(Files.readAllBytes(privateKeyPath(tempDir))).isEqualTo(expectedPrivate);
        assertThat(Files.readAllBytes(publicKeyPath(tempDir))).isEqualTo(expectedPublic);
        assertThat(second.keyPair().keyPair()).isNotNull();
    }

    @Test
    void onlyPrivateKeyExists_failsStartup() throws Exception {
        KeyPair pair = new RsaKeyGenerator(2048).generate();
        Files.writeString(privateKeyPath(tempDir), PemCodec.encodePrivateKey(pair.getPrivate()));

        JwtKeyManager manager = newManager(tempDir);

        assertThatThrownBy(manager::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Incomplete key pair");
        assertThat(publicKeyPath(tempDir)).doesNotExist();
    }

    @Test
    void onlyPublicKeyExists_failsStartup() throws Exception {
        KeyPair pair = new RsaKeyGenerator(2048).generate();
        Files.writeString(publicKeyPath(tempDir), PemCodec.encodePublicKey(pair.getPublic()));

        JwtKeyManager manager = newManager(tempDir);

        assertThatThrownBy(manager::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Incomplete key pair");
        assertThat(privateKeyPath(tempDir)).doesNotExist();
    }

    @Test
    void corruptedPrivateKey_failsStartup() throws Exception {
        KeyPair pair = new RsaKeyGenerator(2048).generate();
        Files.writeString(publicKeyPath(tempDir), PemCodec.encodePublicKey(pair.getPublic()));
        Files.writeString(privateKeyPath(tempDir), "not a valid key");

        JwtKeyManager manager = newManager(tempDir);

        assertThatThrownBy(manager::initialize)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void keysNotSamePair_failsStartup() throws Exception {
        KeyPair pairA = new RsaKeyGenerator(2048).generate();
        KeyPair pairB = new RsaKeyGenerator(2048).generate();
        Files.writeString(privateKeyPath(tempDir), PemCodec.encodePrivateKey(pairA.getPrivate()));
        Files.writeString(publicKeyPath(tempDir), PemCodec.encodePublicKey(pairB.getPublic()));

        JwtKeyManager manager = newManager(tempDir);

        assertThatThrownBy(manager::initialize)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("do not belong to the same RSA key pair");
    }

    @Test
    void generatedPrivateKeyHasRestrictedPermission() throws Exception {
        if (privateKeyPath(tempDir).getFileSystem().supportedFileAttributeViews().contains("posix")) {
            JwtKeyManager manager = newManager(tempDir);
            manager.initialize();
            java.util.Set<java.nio.file.attribute.PosixFilePermission> perms =
                    Files.getPosixFilePermissions(privateKeyPath(tempDir));
            assertThat(perms).doesNotContain(
                    java.nio.file.attribute.PosixFilePermission.GROUP_READ,
                    java.nio.file.attribute.PosixFilePermission.OTHERS_READ,
                    java.nio.file.attribute.PosixFilePermission.GROUP_WRITE,
                    java.nio.file.attribute.PosixFilePermission.OTHERS_WRITE);
        }
    }

    @Test
    void initializeCanOnlyBeCalledOnce() throws Exception {
        JwtKeyManager manager = newManager(tempDir);
        manager.initialize();
        assertThatThrownBy(manager::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("only be called once");
    }

    @Test
    void keyPairUnavailableBeforeInitialization() {
        JwtKeyManager manager = newManager(tempDir);
        assertThatThrownBy(manager::keyPair)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not been initialized");
    }
}
