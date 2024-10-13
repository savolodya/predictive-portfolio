package com.savolodya.predictiveportfolio.repositories;

import com.savolodya.predictiveportfolio.models.actiontoken.ActionToken;
import com.savolodya.predictiveportfolio.models.actiontoken.ActionTokenType;
import com.savolodya.predictiveportfolio.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActionTokenRepository extends JpaRepository<ActionToken, Long> {
    Optional<ActionToken> findByUserAndType(User user, ActionTokenType type);
}
