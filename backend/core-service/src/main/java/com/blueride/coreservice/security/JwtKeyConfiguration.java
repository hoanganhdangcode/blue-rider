package com.blueride.coreservice.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtKeyProperties.class)
public class JwtKeyConfiguration {

    @Bean
    public RsaKeyGenerator rsaKeyGenerator(JwtKeyProperties properties) {
        return new RsaKeyGenerator(properties.getRsaKeySize());
    }

    @Bean
    public JwtKeyManager jwtKeyManager(JwtKeyProperties properties, RsaKeyGenerator generator) {
        JwtKeyManager manager = new JwtKeyManager(properties, generator);
        manager.initialize();
        return manager;
    }

    @Bean
    public JwtService jwtService(JwtKeyManager keyManager) {
        return new JwtService(keyManager);
    }
}
