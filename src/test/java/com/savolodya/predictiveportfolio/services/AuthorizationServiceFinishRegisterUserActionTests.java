package com.savolodya.predictiveportfolio.services;

import com.savolodya.predictiveportfolio.exceptions.ActionTokenNotFoundException;
import com.savolodya.predictiveportfolio.models.actiontoken.ActionToken;
import com.savolodya.predictiveportfolio.models.actiontoken.ActionTokenType;
import com.savolodya.predictiveportfolio.models.user.User;
import com.savolodya.predictiveportfolio.models.user.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceFinishRegisterUserActionTests {

    @InjectMocks
    private AuthorizationService authorizationService;
    @Mock
    private ActionTokenService actionTokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void should_ThrowActionTokenNotFoundException_WhenValidActionTokenNotFound() {
        // given
        UUID registerActionToken = UUID.randomUUID();
        String password = "aA1!aA1!";

        given(actionTokenService.findByTokenAndTypeAndExpiryTimestampAfterNow(registerActionToken, ActionTokenType.USER_REGISTER))
                .willReturn(Optional.empty());

        // when
        Executable executable = () -> authorizationService.finishRegisterUserAction(registerActionToken, password);

        // then
        ActionTokenNotFoundException exception = assertThrows(ActionTokenNotFoundException.class, executable);
        assertThat(exception.getMessage()).isEqualTo("Action Token not found");
    }

    @Test
    void should_ActivateUserAndSetEncodedPasswordAndDeleteTokenAndSendEmail_WhenValidActionTokenFound() {
        // given
        UUID registerActionToken = UUID.randomUUID();
        String password = "aA1!aA1!";

        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .status(UserStatus.PENDING_REGISTER)
                .build();

        ActionToken actionToken = ActionToken.builder()
                .token(registerActionToken)
                .user(user)
                .expiryTimestamp(Instant.now().plus(Duration.ofDays(1)))
                .type(ActionTokenType.USER_REGISTER)
                .build();

        given(actionTokenService.findByTokenAndTypeAndExpiryTimestampAfterNow(registerActionToken, ActionTokenType.USER_REGISTER))
                .willReturn(Optional.of(actionToken));
        given(passwordEncoder.encode(password))
                .willReturn("encodedPassword");

        // when
        authorizationService.finishRegisterUserAction(registerActionToken, password);

        // then
        then(actionTokenService).should(times(1))
                .delete(actionToken);
        then(emailService).should(times(1))
                .sendRegisterAccountConfirmationEmail(user.getEmail());

        assertAll(
                () -> assertThat(user.getPassword()).isEqualTo("encodedPassword"),
                () -> assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE)
        );
    }

}