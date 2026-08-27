package com.blueride.coreservice.domain.entity;

import com.blueride.coreservice.common.AppException;
import com.blueride.coreservice.common.ResponseStatus;

public class User {

    private Long id;
    private String username;
    private String password;

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static User create(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new AppException(ResponseStatus.BAD_REQUEST, "username must not be blank");
        }
        if (password == null || password.length() < 6) {
            throw new AppException(ResponseStatus.BAD_REQUEST, "password must be at least 6 characters");
        }
        return new User(null, username, password);
    }

    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new AppException(ResponseStatus.BAD_REQUEST, "password must be at least 6 characters");
        }
        this.password = newPassword;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
