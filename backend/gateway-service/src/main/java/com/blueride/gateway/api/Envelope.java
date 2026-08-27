package com.blueride.gateway.api;

import java.util.Map;

public record Envelope(boolean success, String action, Object data, ApiError error) {

    public static Envelope ok(String action, Object data) {
        return new Envelope(true, action, data == null ? Map.of() : data, null);
    }

    public static Envelope fail(String action, String code, String message) {
        return new Envelope(false, action, null, new ApiError(code, message));
    }
}
