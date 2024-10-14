package com.savolodya.predictiveportfolio.models.register;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterUserFormValidatorsTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void should_ValidateRegisterUserForm_When_Valid() {
        // given
        RegisterUserForm form = new RegisterUserForm("test@test.com", "aA1!aA1!");

        // when
        Set<ConstraintViolation<RegisterUserForm>> violations = validator.validate(form);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void should_ValidateRegisterUserForm_When_EmailIsNotValid() {
        // given
        RegisterUserForm form = new RegisterUserForm("qwerty", "aA1!aA1!");

        // when
        Set<ConstraintViolation<RegisterUserForm>> violations = validator.validate(form);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Email is not valid");
    }

    @ParameterizedTest(name = "Email length: {0}")
    @ValueSource(ints = {2, 256})
    void should_ValidateRegisterUserForm_When_EmailIsOutOfBound(
            int length
    ) {
        // given
        RegisterUserForm form = new RegisterUserForm(String.format("%s", "a".repeat(length)), "aA1!aA1!");

        // when
        Set<ConstraintViolation<RegisterUserForm>> violations = validator.validate(form);

        // then
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "Email is not valid",
                        "Email has to be between 3 and 255 characters"
                );
    }

    @Test
    void should_ValidateUser_When_PasswordIsNotMatchingRules() {
        // given
        RegisterUserForm form = new RegisterUserForm("test@test.com", "a");

        // when
        Set<ConstraintViolation<RegisterUserForm>> violations = validator.validate(form);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Password does not match rules");
    }


}