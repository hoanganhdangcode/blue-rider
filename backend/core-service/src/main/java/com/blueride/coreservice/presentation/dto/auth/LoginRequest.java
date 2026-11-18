package com.blueride.coreservice.presentation.dto.auth;

import com.blueride.coreservice.presentation.validation.PhoneValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^\\+[1-9]\\d{1,14}$",
        message = "Invalid phone number format"
    )
    @PhoneValid 
    String phoneNumber
) {}