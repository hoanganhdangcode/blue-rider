package com.blueride.coreservice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.blueride.coreservice.common.AppException;
import com.blueride.coreservice.common.ResponseStatus;
import org.junit.jupiter.api.Test;

class OtpServiceTest {

    private OtpService newService(boolean enabled, String code) {
        OtpProperties properties = new OtpProperties();
        properties.setEnabled(enabled);
        properties.setCode(code);
        return new OtpService(properties);
    }

    @Test
    void requestOtp_validPhone_returnsNormalizedPhone() {
        OtpService service = newService(true, "456789");
        assertThat(service.requestOtp("0912345678")).isEqualTo("0912345678");
    }

    @Test
    void requestOtp_validPhoneWithCountryCode_normalizes() {
        OtpService service = newService(true, "456789");
        assertThat(service.requestOtp("+84912345678")).isEqualTo("0912345678");
    }

    @Test
    void requestOtp_invalidPhone_rejected() {
        OtpService service = newService(true, "456789");
        assertThatThrownBy(() -> service.requestOtp("12345"))
                .isInstanceOf(AppException.class)
                .extracting(e -> ((AppException) e).getStatus())
                .isEqualTo(ResponseStatus.BAD_REQUEST);
    }

    @Test
    void requestOtp_blankPhone_rejected() {
        OtpService service = newService(true, "456789");
        assertThatThrownBy(() -> service.requestOtp("  "))
                .isInstanceOf(AppException.class)
                .extracting(e -> ((AppException) e).getStatus())
                .isEqualTo(ResponseStatus.BAD_REQUEST);
    }

    @Test
    void requestOtp_mockDisabled_rejected() {
        OtpService service = newService(false, "456789");
        assertThatThrownBy(() -> service.requestOtp("0912345678"))
                .isInstanceOf(AppException.class)
                .extracting(e -> ((AppException) e).getStatus())
                .isEqualTo(ResponseStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void verifyOtp_correctOtp_succeeds() {
        OtpService service = newService(true, "456789");
        assertThatCode(() -> service.verifyOtp("0912345678", "456789"))
                .doesNotThrowAnyException();
    }

    @Test
    void verifyOtp_wrongOtp_rejected() {
        OtpService service = newService(true, "456789");
        assertThatThrownBy(() -> service.verifyOtp("0912345678", "000000"))
                .isInstanceOf(AppException.class)
                .extracting(e -> ((AppException) e).getStatus())
                .isEqualTo(ResponseStatus.BAD_REQUEST);
    }

    @Test
    void verifyOtp_nullOtp_rejected() {
        OtpService service = newService(true, "456789");
        assertThatThrownBy(() -> service.verifyOtp("0912345678", null))
                .isInstanceOf(AppException.class);
    }

    @Test
    void verifyOtp_invalidPhone_rejected() {
        OtpService service = newService(true, "456789");
        assertThatThrownBy(() -> service.verifyOtp("12", "456789"))
                .isInstanceOf(AppException.class);
    }

    @Test
    void verifyOtp_mockDisabled_evenCorrectOtp_rejected() {
        OtpService service = newService(false, "456789");
        assertThatThrownBy(() -> service.verifyOtp("0912345678", "456789"))
                .isInstanceOf(AppException.class)
                .extracting(e -> ((AppException) e).getStatus())
                .isEqualTo(ResponseStatus.SERVICE_UNAVAILABLE);
    }
}
