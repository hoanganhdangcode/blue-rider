package com.blueride.gateway.api;

public record ApiEnvelope(boolean success, String action, Object data, ApiError error) {

	public static ApiEnvelope ok(String action, Object data) {
		return new ApiEnvelope(true, action, data, null);
	}

	public static ApiEnvelope fail(String action, String code, String message) {
		return new ApiEnvelope(false, action, null, new ApiError(code, message));
	}
}
