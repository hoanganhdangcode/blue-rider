package com.blueride.coreservice.application.service;

public record AuthAccountDto(Long id, String phoneNumber) {

    public static AuthAccountDto of(Long id, String phoneNumber) {
        return new AuthAccountDto(id, phoneNumber);
    }
}
