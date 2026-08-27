package com.blueride.coreservice.application.service;

import com.blueride.coreservice.domain.entity.User;

public record UserResponse(Long id, String username) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }
}
