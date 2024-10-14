package com.savolodya.predictiveportfolio.repositories;

import com.savolodya.predictiveportfolio.models.actiontoken.ActionToken;
import com.savolodya.predictiveportfolio.models.actiontoken.ActionTokenType;
import com.savolodya.predictiveportfolio.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ActionTokenRepository extends JpaRepository<ActionToken, Long> {
    Optional<ActionToken> findByUserAndType(User user, ActionTokenType type);
    Optional<ActionToken> findByTokenAndTypeAndExpiryTimestampAfter(@NonNull UUID token, @NonNull ActionTokenType type, @NonNull Instant expiryTimestamp);
}
