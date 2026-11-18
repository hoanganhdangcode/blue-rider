package com.blueride.coreservice.application.usecase;

import com.blueride.coreservice.presentation.dto.auth.LoginRequest;
import com.blueride.coreservice.presentation.dto.auth.LoginResponse;

public interface LoginUsecase {
    LoginResponse execute(LoginRequest request);
}