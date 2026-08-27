package com.blueride.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record GatewayProperties(String internalApiKey, String authBaseUrl, String coreBaseUrl) {
}
