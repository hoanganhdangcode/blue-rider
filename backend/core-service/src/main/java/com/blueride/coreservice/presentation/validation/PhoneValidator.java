package com.blueride.coreservice.presentation.validation;

import com.blueride.coreservice.presentation.dto.auth.LoginRequest;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator
        implements ConstraintValidator<PhoneValid, String> {

    private final PhoneNumberUtil phoneUtil =
            PhoneNumberUtil.getInstance();

    @Override
    public boolean isValid(
            String phone,
            ConstraintValidatorContext context
    ) {
        if (phone == null) return true;

        try {

            var number = phoneUtil.parse(phone, null);

            return phoneUtil.isValidNumber(number);

        } catch (NumberParseException e) {
            return false;
        }
    }
}