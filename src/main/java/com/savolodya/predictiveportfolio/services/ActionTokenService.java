package com.savolodya.predictiveportfolio.services;

import com.savolodya.predictiveportfolio.models.actiontoken.ActionToken;
import com.savolodya.predictiveportfolio.models.actiontoken.ActionTokenType;
import com.savolodya.predictiveportfolio.models.user.User;
import com.savolodya.predictiveportfolio.repositories.ActionTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActionTokenService {

    private final ActionTokenRepository actionTokenRepository;

    @Value(value = "${token.registration.duration}")
    private Duration registrationTokenDuration;

    public ActionToken createOrResetRegisterAccountToken(User user) {
        // Delete old token for this user if present
        actionTokenRepository.findByUserAndType(user, ActionTokenType.USER_REGISTER)
                .ifPresent(this::delete);

        return save(new ActionToken(user, Instant.now().plus(registrationTokenDuration), ActionTokenType.USER_REGISTER));
    }

    public Optional<ActionToken> findByTokenAndTypeAndExpiryTimestampAfterNow(UUID token, ActionTokenType type) {
        return actionTokenRepository.findByTokenAndTypeAndExpiryTimestampAfter(token, type, Instant.now());
    }

    public void delete(ActionToken actionToken) {
        actionTokenRepository.delete(actionToken);
    }

    private ActionToken save(ActionToken actionToken) {
        return actionTokenRepository.save(actionToken);
    }
}
