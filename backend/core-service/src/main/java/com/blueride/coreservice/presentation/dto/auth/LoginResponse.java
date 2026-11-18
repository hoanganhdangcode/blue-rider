
package com.blueride.coreservice.presentation.dto.auth;

import java.time.Instant;

public record LoginResponse(

    String challengeId,
    String challenge,
    String nonce,
    Instant expiresAt)
{}
