package com.savolodya.predictiveportfolio.models.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserValidatorsTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void should_ValidateUser_When_Valid() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .password("aA1!aA1!")
                .status(UserStatus.ACTIVE)
                .build();

        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void should_ValidateUser_When_UuidIsNull() {
        // given
        User user = User.builder()
                .email("test@test.com")
                .password("aA1!aA1!")
                .status(UserStatus.ACTIVE)
                .build();

        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("UUID has to be set");
    }

    @Test
    void should_ValidateUser_When_EmailIsNull() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .password("aA1!aA1!")
                .status(UserStatus.ACTIVE)
                .build();

        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Email has to be set");
    }

    @Test
    void should_ValidateUser_When_EmailIsNotValid() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("qwerty")
                .password("aA1!aA1!")
                .status(UserStatus.ACTIVE)
                .build();

        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Email is not valid");
    }

    @ParameterizedTest(name = "Email lLength: {0}")
    @ValueSource(ints = {2, 256})
    void should_ValidateUser_When_EmailIsOutOfBound(
            int length
    ) {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email(String.format("%s", "a".repeat(length)))
                .password("aA1!aA1!")
                .status(UserStatus.ACTIVE)
                .build();

        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // then
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "Email is not valid",
                        "Email has to be between 3 and 255 characters"
                );
    }

    @Test
    void should_ValidateUser_When_PasswordIsNull() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .status(UserStatus.ACTIVE)
                .build();

        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Password has to be set");
    }

    @Test
    void should_ValidateUser_When_PasswordIsNotMatchingRules() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .password("a")
                .status(UserStatus.ACTIVE)
                .build();

        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Password does not match rules");
    }

    @Test
    void should_ValidateUser_When_StatusIsNull() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .password("aA1!aA1!")
                .build();

        // when
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Status has to be set");
    }


}