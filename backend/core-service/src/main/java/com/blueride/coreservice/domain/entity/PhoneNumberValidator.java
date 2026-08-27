package com.blueride.coreservice.domain.entity;

import com.blueride.coreservice.common.AppException;
import com.blueride.coreservice.common.ResponseStatus;

/**
 * Validates Vietnamese mobile phone numbers for the MVP. Accepts an optional
 * leading '+' country code and normalizes to the local 0-format.
 */
public final class PhoneNumberValidator {

    private static final java.util.regex.Pattern VIETNAM_MOBILE =
            java.util.regex.Pattern.compile("^0\\d{9}$");

    private PhoneNumberValidator() {
    }

    public static String normalize(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new AppException(ResponseStatus.BAD_REQUEST, "phoneNumber must not be blank");
        }
        String trimmed = phoneNumber.trim();
        if (trimmed.startsWith("+84")) {
            trimmed = "0" + trimmed.substring(3);
        } else if (trimmed.startsWith("84") && trimmed.length() == 11) {
            trimmed = "0" + trimmed.substring(2);
        }
        if (!VIETNAM_MOBILE.matcher(trimmed).matches()) {
            throw new AppException(ResponseStatus.BAD_REQUEST,
                    "Invalid phone number: " + phoneNumber);
        }
        return trimmed;
    }
}
