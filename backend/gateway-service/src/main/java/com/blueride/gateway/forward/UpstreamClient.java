package com.blueride.gateway.forward;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.blueride.gateway.api.Envelope;
import com.blueride.gateway.catalog.ActionDefinition;
import com.blueride.gateway.config.GatewayProperties;

@Component
public class UpstreamClient {

    private final RestClient restClient;
    private final GatewayProperties properties;
    private final ObjectMapper objectMapper;

    public UpstreamClient(RestClient restClient, GatewayProperties properties, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public Envelope forward(String action, ActionDefinition definition) {
        String baseUrl = resolveBase(definition.upstream());
        if (baseUrl == null) {
            return Envelope.fail(action, "UNKNOWN_UPSTREAM", "Unknown upstream");
        }
        String url = trimTrailingSlash(baseUrl) + definition.path();
        try {
            return restClient.method(HttpMethod.valueOf(definition.method().toUpperCase(Locale.ROOT)))
                    .uri(url)
                    .header("X-Internal-Key", properties.internalApiKey())
                    .exchange((request, response) -> {
                        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                        if (!response.getStatusCode().is2xxSuccessful()) {
                            return Envelope.fail(
                                    action,
                                    "UPSTREAM_ERROR",
                                    "Upstream returned " + response.getStatusCode().value()
                            );
                        }
                        return Envelope.ok(action, parseBody(body));
                    });
        } catch (RestClientException ex) {
            return Envelope.fail(action, "UPSTREAM_ERROR", "Upstream unavailable");
        }
    }

    private String resolveBase(String upstream) {
        if ("auth".equals(upstream)) {
            return properties.authBaseUrl();
        }
        if ("core".equals(upstream)) {
            return properties.coreBaseUrl();
        }
        return null;
    }

    private static String trimTrailingSlash(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

    private Object parseBody(String body) {
        if (body == null || body.isBlank()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(body, Object.class);
        } catch (Exception ex) {
            return body;
        }
    }
}
