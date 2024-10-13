package com.savolodya.predictiveportfolio.repositories;

import com.savolodya.predictiveportfolio.models.actiontoken.ActionToken;
import com.savolodya.predictiveportfolio.models.actiontoken.ActionTokenType;
import com.savolodya.predictiveportfolio.models.user.User;
import com.savolodya.predictiveportfolio.models.user.UserStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(value = false)
class ActionTokenRepositoryTests {

    @Autowired
    private ActionTokenRepository actionTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void should_CreateActionToken() {
        // given
        User user = User.builder()
                .email("test@test.com")
                .password("aA1!aA1!")
                .uuid(UUID.randomUUID())
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(user);

        ActionToken actionToken = ActionToken.builder()
                .token(UUID.randomUUID())
                .user(user)
                .expiryTimestamp(Instant.now().plus(Duration.ofDays(1)))
                .type(ActionTokenType.USER_REGISTER)
                .build();

        // when
        actionTokenRepository.save(actionToken);

        // then
        assertThat(actionToken.getId()).isPositive();
    }

    @Test
    @Order(2)
    void should_FindActionToken() {
        // when
        ActionToken actionToken = actionTokenRepository.findById(1L).get();

        // then
        assertThat(actionToken.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    void should_FindActionTokenByUserAndType() {
        // given
        User user = userRepository.findByEmail("test@test.com").get();

        // when
        ActionToken actionToken = actionTokenRepository.findByUserAndType(user, ActionTokenType.USER_REGISTER).get();

        // then
        assertThat(actionToken.getId()).isEqualTo(1L);
    }

    @Test
    @Order(4)
    void should_FindAllActionTokens() {
        // when
        List<ActionToken> actionTokens = actionTokenRepository.findAll();

        // hen
        assertThat(actionTokens).isNotEmpty();
    }

    @Test
    @Order(5)
    void should_DeleteActionToken() {
        // when
        actionTokenRepository.deleteById(1L);
        Optional<ActionToken> actionTokenOptional = actionTokenRepository.findById(1L);

        // then
        assertThat(actionTokenOptional).isEmpty();
    }
}