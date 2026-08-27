package com.blueride.coreservice.common;

public class BaseResponse<T> {

    private int statusCode;
    private String message;
    private T data;

    public BaseResponse() {
    }

    private BaseResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), data);
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(ResponseStatus.SUCCESS.getCode(), message, data);
    }

    public static <T> BaseResponse<T> error(int statusCode, String message) {
        return new BaseResponse<>(statusCode, message, null);
    }

    public static <T> BaseResponse<T> error(ResponseStatus status) {
        return new BaseResponse<>(status.getCode(), status.getMessage(), null);
    }

    public static <T> BaseResponse<T> error(ResponseStatus status, String message) {
        return new BaseResponse<>(status.getCode(), message, null);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
