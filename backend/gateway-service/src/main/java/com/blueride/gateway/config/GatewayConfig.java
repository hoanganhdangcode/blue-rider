package com.blueride.gateway.config;

import java.security.KeyPair;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.blueride.crypto.JwtService;
import com.blueride.crypto.PasswordHasher;
import com.blueride.crypto.RsaKeys;

@Configuration
@EnableConfigurationProperties(GatewayProperties.class)
public class GatewayConfig {

	@Bean
	PasswordHasher passwordHasher() {
		return new PasswordHasher();
	}

	@Bean
	JwtService jwtService(
			@Value("${jwt.private-key-pem:}") String privatePem,
			@Value("${jwt.public-key-pem:}") String publicPem) {
		KeyPair keyPair;
		if (privatePem == null || privatePem.isBlank() || publicPem == null || publicPem.isBlank()) {
			keyPair = RsaKeys.generate();
		} else {
			keyPair = RsaKeys.fromPem(privatePem, publicPem);
		}
		return new JwtService(keyPair);
	}

	@Bean
	RestClient restClient(GatewayProperties properties) {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(Duration.ofMillis(properties.getConnectTimeoutMs()));
		factory.setReadTimeout(Duration.ofMillis(properties.getReadTimeoutMs()));
		return RestClient.builder().requestFactory(factory).build();
	}
}
