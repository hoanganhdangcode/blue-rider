package com.blueride.coreservice.application.service;

import com.blueride.coreservice.common.AppException;
import com.blueride.coreservice.common.ResponseStatus;
import com.blueride.coreservice.domain.entity.Account;
import com.blueride.coreservice.domain.repository.AccountRepository;
import com.blueride.coreservice.security.JwtService;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final OtpService otpService;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final long expiresInSeconds;

    public AuthService(OtpService otpService, AccountRepository accountRepository,
                       JwtService jwtService) {
        this.otpService = otpService;
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
        this.expiresInSeconds = JwtService.DEFAULT_EXPIRES_IN_SECONDS;
    }

    public void requestOtp(String phoneNumber) {
        otpService.requestOtp(phoneNumber);
    }

    @Transactional
    public AuthResponse verifyOtp(String phoneNumber, String otp) {
        otpService.verifyOtp(phoneNumber, otp);

        String normalized = phoneNumber.trim();
        if (normalized.startsWith("+84")) {
            normalized = "0" + normalized.substring(3);
        }

        Account account = accountRepository.findOrCreateByPhoneNumber(normalized);

        if (account.getStatus() != Account.Status.ACTIVE) {
            throw new AppException(ResponseStatus.FORBIDDEN, "Account is not active");
        }

        String token = jwtService.sign(
                String.valueOf(account.getId()),
                Map.of("phone", account.getPhoneNumber()),
                expiresInSeconds);

        return AuthResponse.of(token, expiresInSeconds,
                AuthAccountDto.of(account.getId(), account.getPhoneNumber()));
    }
}
