package com.blueride.coreservice.application.usecase;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.blueride.coreservice.presentation.dto.auth.LoginRequest;
import com.blueride.coreservice.presentation.dto.auth.LoginResponse;

@Service 
public class LoginUsecaseImpl implements LoginUsecase {
    @Override
    public LoginResponse execute(LoginRequest request) {
        return new LoginResponse("a", "b", "c", Instant.now());
    }
}
