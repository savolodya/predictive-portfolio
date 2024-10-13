package com.savolodya.predictiveportfolio.models.actiontoken;

import com.savolodya.predictiveportfolio.models.user.User;
import com.savolodya.predictiveportfolio.models.user.UserStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ActionTokenValidatorsTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void should_ValidateActionToken_When_Valid() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .password("aA1!aA1!")
                .status(UserStatus.ACTIVE)
                .build();

        ActionToken actionToken = ActionToken.builder()
                .token(UUID.randomUUID())
                .user(user)
                .expiryTimestamp(Instant.now().plus(Duration.ofDays(1)))
                .type(ActionTokenType.USER_REGISTER)
                .build();

        // when
        Set<ConstraintViolation<ActionToken>> violations = validator.validate(actionToken);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void should_ValidateActionToken_When_TokenIsNull() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .password("aA1!aA1!")
                .status(UserStatus.ACTIVE)
                .build();

        ActionToken actionToken = ActionToken.builder()
                .user(user)
                .expiryTimestamp(Instant.now().plus(Duration.ofDays(1)))
                .type(ActionTokenType.USER_REGISTER)
                .build();

        // when
        Set<ConstraintViolation<ActionToken>> violations = validator.validate(actionToken);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Token has to be set");
    }

    @Test
    void should_ValidateActionToken_When_UserIsNull() {
        // given
        ActionToken actionToken = ActionToken.builder()
                .token(UUID.randomUUID())
                .expiryTimestamp(Instant.now().plus(Duration.ofDays(1)))
                .type(ActionTokenType.USER_REGISTER)
                .build();

        // when
        Set<ConstraintViolation<ActionToken>> violations = validator.validate(actionToken);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("User has to be set");
    }

    @Test
    void should_ValidateActionToken_When_expiryTimestampIsNull() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .password("aA1!aA1!")
                .status(UserStatus.ACTIVE)
                .build();

        ActionToken actionToken = ActionToken.builder()
                .token(UUID.randomUUID())
                .user(user)
                .type(ActionTokenType.USER_REGISTER)
                .build();

        // when
        Set<ConstraintViolation<ActionToken>> violations = validator.validate(actionToken);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Expiry timestamp has to be set");
    }

    @Test
    void should_ValidateActionToken_When_expiryTimestampIsNotInTheFuture() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .password("aA1!aA1!")
                .status(UserStatus.ACTIVE)
                .build();

        ActionToken actionToken = ActionToken.builder()
                .token(UUID.randomUUID())
                .user(user)
                .expiryTimestamp(Instant.now().minus(Duration.ofDays(1)))
                .type(ActionTokenType.USER_REGISTER)
                .build();

        // when
        Set<ConstraintViolation<ActionToken>> violations = validator.validate(actionToken);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Expiry timestamp has to be in the future");
    }

    @Test
    void should_ValidateActionToken_When_typeIsNull() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .password("aA1!aA1!")
                .status(UserStatus.ACTIVE)
                .build();

        ActionToken actionToken = ActionToken.builder()
                .token(UUID.randomUUID())
                .user(user)
                .expiryTimestamp(Instant.now().plus(Duration.ofDays(1)))
                .build();

        // when
        Set<ConstraintViolation<ActionToken>> violations = validator.validate(actionToken);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Type has to be set");
    }
}