package com.blueride.coreservice.application.service;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        AuthAccountDto account) {

    public static AuthResponse of(String accessToken, long expiresIn, AuthAccountDto account) {
        return new AuthResponse(accessToken, "Bearer", expiresIn, account);
    }
}
