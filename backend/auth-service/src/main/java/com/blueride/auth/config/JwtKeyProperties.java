package com.blueride.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtKeyProperties(String privateKeyPem, String publicKeyPem) {
}
