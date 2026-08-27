package com.blueride.coreservice.common;

public class AppException extends RuntimeException {

    private final ResponseStatus status;

    public AppException(ResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }

    public AppException(ResponseStatus status, String message) {
        super(message);
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return status;
    }
}
