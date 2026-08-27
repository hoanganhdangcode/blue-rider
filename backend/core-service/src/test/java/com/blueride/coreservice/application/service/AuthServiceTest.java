package com.blueride.coreservice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.blueride.coreservice.common.AppException;
import com.blueride.coreservice.common.ResponseStatus;
import com.blueride.coreservice.domain.entity.Account;
import com.blueride.coreservice.domain.repository.AccountRepository;
import com.blueride.coreservice.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtService jwtService;

    @Test
    void verifyOtp_newAccount_createdAndTokenIssued() {
        AuthResponse response = authService.verifyOtp("0987654321", "456789");

        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresIn()).isEqualTo(JwtService.DEFAULT_EXPIRES_IN_SECONDS);
        assertThat(response.account().phoneNumber()).isEqualTo("0987654321");
        assertThat(accountRepository.existsByPhoneNumber("0987654321")).isTrue();

        var claims = jwtService.verify(response.accessToken());
        assertThat(claims.getSubject()).isEqualTo(response.account().id().toString());
        assertThat(claims.get("phone")).isEqualTo("0987654321");
    }

    @Test
    @Transactional
    void verifyOtp_existingAccount_reusesAccount() {
        AuthResponse first = authService.verifyOtp("0911111111", "456789");
        Account firstAccount = accountRepository.findByPhoneNumber("0911111111").orElseThrow();

        AuthResponse second = authService.verifyOtp("0911111111", "456789");

        Account secondAccount = accountRepository.findByPhoneNumber("0911111111").orElseThrow();
        assertThat(secondAccount.getId()).isEqualTo(firstAccount.getId());
        assertThat(second.accessToken()).isNotBlank();
        assertThat(second.account().id()).isEqualTo(first.account().id());
    }

    @Test
    void verifyOtp_wrongOtp_failsWithoutCreatingAccount() {
        assertThatThrownBy(() -> authService.verifyOtp("0922222222", "000000"))
                .isInstanceOf(AppException.class)
                .extracting(e -> ((AppException) e).getStatus())
                .isEqualTo(ResponseStatus.BAD_REQUEST);

        assertThat(accountRepository.existsByPhoneNumber("0922222222")).isFalse();
    }

    @Test
    void verifyOtp_invalidPhone_fails() {
        assertThatThrownBy(() -> authService.verifyOtp("123", "456789"))
                .isInstanceOf(AppException.class)
                .extracting(e -> ((AppException) e).getStatus())
                .isEqualTo(ResponseStatus.BAD_REQUEST);
    }

    @Test
    void concurrentVerifyOtp_createsExactlyOneAccount() throws Exception {
        String phone = "0933333333";
        int threads = 16;
        var start = new java.util.concurrent.CountDownLatch(1);
        var errors = java.util.concurrent.ConcurrentHashMap.newKeySet();

        Runnable task = () -> {
            try {
                start.await();
                authService.verifyOtp(phone, "456789");
            } catch (Exception e) {
                if (!(e instanceof org.springframework.dao.DataIntegrityViolationException)) {
                    errors.add(e);
                }
            }
        };

        var executor = java.util.concurrent.Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executor.submit(task);
        }
        start.countDown();
        executor.shutdown();
        executor.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS);

        assertThat(errors).isEmpty();
        assertThat(accountRepository.countByPhoneNumber(phone)).isEqualTo(1);
    }
}
