package com.savolodya.predictiveportfolio.services;

import com.savolodya.predictiveportfolio.models.actiontoken.ActionToken;
import com.savolodya.predictiveportfolio.models.actiontoken.ActionTokenType;
import com.savolodya.predictiveportfolio.models.user.Role;
import com.savolodya.predictiveportfolio.models.user.User;
import com.savolodya.predictiveportfolio.models.user.UserRole;
import com.savolodya.predictiveportfolio.models.user.UserStatus;
import com.savolodya.predictiveportfolio.repositories.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceCreateRegisterUserActionTest {

    @InjectMocks
    private AuthorizationService authorizationService;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserService userService;
    @Mock
    private ActionTokenService actionTokenService;
    @Mock
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void should_OnlyCreateUserAndActionTokenAndSendToEmail_When_UserDoesNotExists() {
        // given
        String email = "test@test.com";
        UserRole adminRole = UserRole.builder()
                .name(Role.ADMIN)
                .build();
        ActionToken actionToken = ActionToken.builder()
                .token(UUID.randomUUID())
                .type(ActionTokenType.USER_REGISTER)
                .expiryTimestamp(Instant.now().plus(Duration.ofDays(1)))
                .build();

        User createdUser = User.builder()
                .uuid(UUID.randomUUID())
                .email(email)
                .status(UserStatus.PENDING_REGISTER)
                .build();

        given(userRoleRepository.findByName(Role.ADMIN))
                .willReturn(Optional.of(adminRole));
        given(userService.loadUserByEmail(email))
                .willReturn(Optional.empty());
        given(userService.save(any()))
                .willReturn(createdUser);
        given(actionTokenService.createOrResetRegisterAccountToken(any()))
                .willReturn(actionToken);

        // when
        authorizationService.createRegisterUserAction(email);

        // then
        then(userService).should(never())
                .deleteAndFlush(any());
        then(userService).should(times(1))
                .save(userCaptor.capture());
        then(emailService).should(times(1))
                .sendRegisterAccountEmail(eq(email), any());

        User capturedUser = userCaptor.getValue();
        assertAll(
                () -> assertEquals(email, capturedUser.getEmail()),
                () -> assertNull(capturedUser.getPassword()),
                () -> assertEquals(List.of(adminRole), capturedUser.getRoles()),
                () -> assertEquals(UserStatus.PENDING_REGISTER, capturedUser.getStatus())
        );
    }

    @Test
    void should_DeleteUserAndRegisterAndCreateUserAndActionTokenAndSendToEmail_When_UserIsPresentAndHasStatusPending() {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .status(UserStatus.PENDING_REGISTER)
                .build();
        UserRole adminRole = UserRole.builder()
                .name(Role.ADMIN)
                .build();
        ActionToken actionToken = ActionToken.builder()
                .token(UUID.randomUUID())
                .type(ActionTokenType.USER_REGISTER)
                .expiryTimestamp(Instant.now().plus(Duration.ofDays(1)))
                .build();

        given(userRoleRepository.findByName(Role.ADMIN))
                .willReturn(Optional.of(adminRole));
        given(userService.loadUserByEmail(user.getEmail()))
                .willReturn(Optional.of(user));
        given(userService.save(any()))
                .willReturn(user);
        given(actionTokenService.createOrResetRegisterAccountToken(any()))
                .willReturn(actionToken);

        // when
        authorizationService.createRegisterUserAction(user.getEmail());

        // then
        then(userService).should(times(1))
                .deleteAndFlush(user);
        then(userService).should(times(1))
                .save(userCaptor.capture());
        then(emailService).should(times(1))
                .sendRegisterAccountEmail(eq(user.getEmail()), any());

        User capturedUser = userCaptor.getValue();
        assertAll(
                () -> assertEquals(user.getEmail(), capturedUser.getEmail()),
                () -> assertNull(capturedUser.getPassword()),
                () -> assertEquals(List.of(adminRole), capturedUser.getRoles()),
                () -> assertEquals(UserStatus.PENDING_REGISTER, capturedUser.getStatus())
        );
    }

    @ParameterizedTest(name = "User status: {0}")
    @ValueSource(strings = {"ACTIVE", "INVITED", "LOCKED"})
    void should_DoNothing_When_UserIsPresentAndHasNotStatusPending(
            String status
    ) {
        // given
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@test.com")
                .status(UserStatus.valueOf(status))
                .build();
        UserRole adminRole = UserRole.builder()
                .name(Role.ADMIN)
                .build();

        given(userRoleRepository.findByName(Role.ADMIN))
                .willReturn(Optional.of(adminRole));
        given(userService.loadUserByEmail(user.getEmail()))
                .willReturn(Optional.of(user));

        // when
        authorizationService.createRegisterUserAction(user.getEmail());

        // then
        then(userService).should(never())
                .deleteAndFlush(user);
        then(userService).should(never())
                .save(userCaptor.capture());
        then(emailService).should(never())
                .sendRegisterAccountEmail(any(), any());
    }
}