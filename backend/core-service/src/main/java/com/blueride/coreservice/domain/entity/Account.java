package com.blueride.coreservice.domain.entity;

import com.blueride.coreservice.common.AppException;
import com.blueride.coreservice.common.ResponseStatus;
import java.time.Instant;

public class Account {

    public enum Status {
        ACTIVE,
        BLOCKED
    }

    private Long id;
    private String phoneNumber;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;

    public Account(Long id, String phoneNumber, Status status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Account create(String phoneNumber) {
        String normalized = PhoneNumberValidator.normalize(phoneNumber);
        return new Account(null, normalized, Status.ACTIVE, Instant.now(), Instant.now());
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Status getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
