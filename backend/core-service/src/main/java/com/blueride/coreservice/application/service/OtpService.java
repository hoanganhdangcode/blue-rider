package com.blueride.coreservice.application.service;

import com.blueride.coreservice.common.AppException;
import com.blueride.coreservice.common.ResponseStatus;
import com.blueride.coreservice.domain.entity.PhoneNumberValidator;
import org.springframework.stereotype.Service;

/**
 * OTP issuing/verification for the MVP. No real SMS provider is integrated yet: a
 * Mock OTP (configurable via {@code auth.otp.mock.*}, default {@code 456789}) is
 * accepted. When the mock is disabled, no OTP can be issued and any verification
 * fails. The mock code is never exposed in any response.
 */
@Service
public class OtpService {

    private final OtpProperties properties;

    public OtpService(OtpProperties properties) {
        this.properties = properties;
    }

    /** Validates the phone and issues an OTP challenge. Returns how it was delivered (mock only). */
    public String requestOtp(String phoneNumber) {
        String normalized = PhoneNumberValidator.normalize(phoneNumber);
        if (!properties.isEnabled()) {
            throw new AppException(ResponseStatus.SERVICE_UNAVAILABLE, "OTP is disabled");
        }
        return normalized;
    }

    /** Verifies that the submitted OTP matches the expected (mock) code. */
    public void verifyOtp(String phoneNumber, String otp) {
        PhoneNumberValidator.normalize(phoneNumber);
        if (!properties.isEnabled()) {
            throw new AppException(ResponseStatus.SERVICE_UNAVAILABLE, "OTP is disabled");
        }
        if (otp == null || !otp.equals(properties.getCode())) {
            throw new AppException(ResponseStatus.BAD_REQUEST, "Invalid OTP");
        }
    }
}
