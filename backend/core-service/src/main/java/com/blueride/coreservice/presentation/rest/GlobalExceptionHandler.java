package com.blueride.coreservice.presentation.rest;

import com.blueride.coreservice.common.AppException;
import com.blueride.coreservice.common.BaseResponse;
import com.blueride.coreservice.common.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<BaseResponse<Void>> handleAppException(AppException ex) {
        ResponseStatus status = ex.getStatus();
        return ResponseEntity.status(status.getCode())
                .body(BaseResponse.error(status, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse(ResponseStatus.BAD_REQUEST.getMessage());
        return ResponseEntity.badRequest()
                .body(BaseResponse.error(ResponseStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(BaseResponse.error(ResponseStatus.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleGeneric(Exception ex) {
        return ResponseEntity.status(ResponseStatus.INTERNAL_SERVER_ERROR.getCode())
                .body(BaseResponse.error(ResponseStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }
}
