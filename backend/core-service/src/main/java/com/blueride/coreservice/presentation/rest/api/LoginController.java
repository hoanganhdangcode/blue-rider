package com.blueride.coreservice.presentation.rest.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blueride.coreservice.application.usecase.LoginUsecase;
import com.blueride.coreservice.presentation.dto.auth.LoginRequest;
import com.blueride.coreservice.presentation.dto.auth.LoginResponse;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor

public class LoginController {
        private final LoginUsecase loginUseCase;


    @PostMapping
    public LoginResponse login(
        @Valid @RequestBody LoginRequest loginRequest
    ) {
        return loginUseCase.execute(loginRequest);
    }

}