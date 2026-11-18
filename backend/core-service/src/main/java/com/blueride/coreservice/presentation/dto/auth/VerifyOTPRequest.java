package com.blueride.coreservice.presentation.dto.auth;
    
public record VerifyOTPRequest(
    String otpId,
    String otpEncrypted
){}

