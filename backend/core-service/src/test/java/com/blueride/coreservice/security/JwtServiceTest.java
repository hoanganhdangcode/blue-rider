package com.blueride.coreservice.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JwtServiceTest {

    @TempDir
    Path tempDir;

    private JwtService newService() {
        JwtKeyProperties properties = new JwtKeyProperties();
        properties.setDirectory(tempDir.toString());
        JwtKeyManager manager = new JwtKeyManager(properties, new RsaKeyGenerator(2048));
        manager.initialize();
        return new JwtService(manager);
    }

    @Test
    void signAndVerifyRoundTrip() {
        JwtService jwtService = newService();

        String token = jwtService.sign("user-123", Map.of("role", "CUSTOMER"));

        Claims claims = jwtService.verify(token);
        assertThat(claims.getSubject()).isEqualTo("user-123");
        assertThat(claims.get("role")).isEqualTo("CUSTOMER");
        assertThat(claims.getIssuer()).isEqualTo(JwtService.ISSUER);
    }

    @Test
    void tokenSurvivesReloadFromDisk() {
        JwtService jwtService = newService();
        String token = jwtService.sign("user-456", Map.of());
        String kid = jwtService.kid();

        JwtKeyProperties properties = new JwtKeyProperties();
        properties.setDirectory(tempDir.toString());
        JwtKeyManager reloaded = new JwtKeyManager(properties, new RsaKeyGenerator(2048));
        reloaded.initialize();
        JwtService reloadedService = new JwtService(reloaded);

        Claims claims = reloadedService.verify(token);
        assertThat(claims.getSubject()).isEqualTo("user-456");
        assertThat(kid).isEqualTo(reloadedService.kid());
    }

    @Test
    void generateToken_returnsNonBlankJwt() {
        JwtService jwtService = newService();
        String token = jwtService.sign("user-1", Map.of());
        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void verifyRejectsInvalidSignature() {
        JwtService jwtService = newService();
        String token = jwtService.sign("user-1", Map.of());

        JwtKeyProperties otherProps = new JwtKeyProperties();
        otherProps.setDirectory(tempDir.resolve("other").toString());
        JwtKeyManager other = new JwtKeyManager(otherProps, new RsaKeyGenerator(2048));
        other.initialize();
        JwtService otherService = new JwtService(other);

        assertThatThrownBy(() -> otherService.verify(token))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void verifyRejectsExpiredToken() throws Exception {
        JwtService jwtService = newService();
        String token = jwtService.sign("user-1", Map.of(), -10L);

        assertThatThrownBy(() -> jwtService.verify(token))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void tokenContainsKeyIdHeader() throws Exception {
        JwtService jwtService = newService();
        String token = jwtService.sign("user-789", Map.of());
        String headerJson = token.substring(0, token.indexOf('.'));
        String decoded = new String(java.util.Base64.getUrlDecoder().decode(headerJson),
                java.nio.charset.StandardCharsets.UTF_8);
        assertThat(decoded).contains("\"kid\":\"" + jwtService.kid() + "\"");
    }
}
