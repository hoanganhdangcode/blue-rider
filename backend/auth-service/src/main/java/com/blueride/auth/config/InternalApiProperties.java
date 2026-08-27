package com.blueride.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record InternalApiProperties(String internalApiKey) {
}
