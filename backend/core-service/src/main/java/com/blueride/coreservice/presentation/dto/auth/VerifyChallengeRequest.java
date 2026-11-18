package com.blueride.coreservice.presentation.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record VerifyChallengeRequest (
    @NotBlank(message = "ChallengeId is required")
     String ChallengeId,
    @NotBlank(message = "Challenge answer is required")
     String challengeAnswer
)
{}
