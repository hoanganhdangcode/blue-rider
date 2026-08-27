package com.blueride.coreservice.infrastructure.crypto;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.blueride.crypto.JwtService;
import com.blueride.crypto.PasswordHasher;
import com.blueride.crypto.RsaKeys;

@Configuration
public class CryptoConfig {

	@Bean
	PasswordHasher passwordHasher() {
		return new PasswordHasher();
	}

	@Bean
	JwtService jwtService(
			@Value("${jwt.private-key-pem:}") String privatePem,
			@Value("${jwt.public-key-pem:}") String publicPem) {
		KeyPair keyPair = loadOrGenerate(privatePem, publicPem);
		return new JwtService(keyPair);
	}

	private static KeyPair loadOrGenerate(String privatePem, String publicPem) {
		if (privatePem == null || privatePem.isBlank() || publicPem == null || publicPem.isBlank()) {
			return RsaKeys.generate();
		}
		return RsaKeys.fromPem(privatePem, publicPem);
	}
}
