package com.blueride.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {

	private String internalApiKey = "dev-internal-key";
	private String coreBaseUrl = "http://localhost:8081";
	private String operatorBaseUrl = "http://localhost:8082";
	private int connectTimeoutMs = 3_000;
	private int readTimeoutMs = 5_000;

	public String getInternalApiKey() {
		return internalApiKey;
	}

	public void setInternalApiKey(String internalApiKey) {
		this.internalApiKey = internalApiKey;
	}

	public String getCoreBaseUrl() {
		return coreBaseUrl;
	}

	public void setCoreBaseUrl(String coreBaseUrl) {
		this.coreBaseUrl = coreBaseUrl;
	}

	public String getOperatorBaseUrl() {
		return operatorBaseUrl;
	}

	public void setOperatorBaseUrl(String operatorBaseUrl) {
		this.operatorBaseUrl = operatorBaseUrl;
	}

	public int getConnectTimeoutMs() {
		return connectTimeoutMs;
	}

	public void setConnectTimeoutMs(int connectTimeoutMs) {
		this.connectTimeoutMs = connectTimeoutMs;
	}

	public int getReadTimeoutMs() {
		return readTimeoutMs;
	}

	public void setReadTimeoutMs(int readTimeoutMs) {
		this.readTimeoutMs = readTimeoutMs;
	}

	public String baseUrl(String service) {
		if ("core".equals(service)) {
			return trimSlash(coreBaseUrl);
		}
		if ("operator".equals(service)) {
			return trimSlash(operatorBaseUrl);
		}
		throw new IllegalArgumentException("Unknown upstream service: " + service);
	}

	private static String trimSlash(String url) {
		if (url == null || url.isBlank()) {
			return "";
		}
		return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
	}
}
