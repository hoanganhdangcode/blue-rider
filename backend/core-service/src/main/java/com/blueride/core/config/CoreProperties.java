package com.blueride.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record CoreProperties(String internalApiKey, String authBaseUrl) {
}
