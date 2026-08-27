package com.blueride.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.blueride.crypto.jwt.RsaJwtService;
import com.blueride.crypto.jwt.RsaKeyPairs;
import com.blueride.crypto.password.Argon2idPasswordHasher;
import com.blueride.crypto.password.PasswordHasher;

@Configuration
@EnableConfigurationProperties({InternalApiProperties.class, JwtKeyProperties.class})
public class AuthConfig {

    @Bean
    PasswordHasher passwordHasher() {
        return new Argon2idPasswordHasher();
    }

    @Bean
    RsaJwtService rsaJwtService(JwtKeyProperties jwtKeyProperties) {
        return new RsaJwtService(RsaKeyPairs.loadOrEphemeral(
                jwtKeyProperties.privateKeyPem(),
                jwtKeyProperties.publicKeyPem()
        ));
    }
}
