package com.savolodya.predictiveportfolio.services;

import com.savolodya.predictiveportfolio.models.actiontoken.ActionToken;
import com.savolodya.predictiveportfolio.models.actiontoken.ActionTokenType;
import com.savolodya.predictiveportfolio.models.user.User;
import com.savolodya.predictiveportfolio.repositories.ActionTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ActionTokenServiceCreateOrResetRegisterAccountTokenTests {

    @InjectMocks
    private ActionTokenService actionTokenService;
    @Mock
    private ActionTokenRepository actionTokenRepository;

    @BeforeEach
    public void setUp() throws Exception {
        Field field = actionTokenService.getClass().getDeclaredField("registrationTokenDuration");
        field.setAccessible(true);
        field.set(actionTokenService, Duration.ofDays(1));
    }

    @Test
    void should_OnlyCreateActionToken_When_TokenWithSameUserAndTypeIsNotPresent() {
        // given
        User user = new User();

        given(actionTokenRepository.findByUserAndType(user, ActionTokenType.USER_REGISTER))
                .willReturn(Optional.empty());

        // when
        actionTokenService.createOrResetRegisterAccountToken(user);

        // then
        then(actionTokenRepository).should(never())
                .delete(any());
        then(actionTokenRepository).should(times(1))
                .save(any());
    }

    @Test
    void should_DeleteOldAndCreateNewActionToken_When_TokenWithSameUserAndTypeIsPresent() {
        // given
        User user = new User();

        given(actionTokenRepository.findByUserAndType(user, ActionTokenType.USER_REGISTER))
                .willReturn(Optional.of(new ActionToken()));

        // when
        actionTokenService.createOrResetRegisterAccountToken(user);

        // then
        then(actionTokenRepository).should(times(1))
                .delete(any());
        then(actionTokenRepository).should(times(1))
                .save(any());
    }

}