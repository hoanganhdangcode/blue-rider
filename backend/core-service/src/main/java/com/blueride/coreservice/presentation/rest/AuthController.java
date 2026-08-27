package com.blueride.coreservice.presentation.rest;

import com.blueride.coreservice.application.service.AuthResponse;
import com.blueride.coreservice.application.service.AuthService;
import com.blueride.coreservice.common.BaseResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/request-otp")
    public BaseResponse<Void> requestOtp(@Valid @RequestBody OtpRequest request) {
        authService.requestOtp(request.phoneNumber());
        return BaseResponse.success("OTP sent", null);
    }

    @PostMapping("/verify-otp")
    public BaseResponse<AuthResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        AuthResponse response = authService.verifyOtp(request.phoneNumber(), request.otp());
        return BaseResponse.success(response);
    }

    public record OtpRequest(
            @NotBlank(message = "must not be blank") String phoneNumber) {
    }

    public record VerifyOtpRequest(
            @NotBlank(message = "must not be blank") String phoneNumber,
            @NotBlank(message = "must not be blank") String otp) {
    }
}
