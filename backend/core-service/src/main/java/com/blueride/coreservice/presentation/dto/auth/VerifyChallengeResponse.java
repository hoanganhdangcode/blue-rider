
package com.blueride.coreservice.presentation.dto.auth;
    
import java.time.Instant;

public record VerifyChallengeResponse(
    String otpId,
    Instant reSendAt
)
{}

