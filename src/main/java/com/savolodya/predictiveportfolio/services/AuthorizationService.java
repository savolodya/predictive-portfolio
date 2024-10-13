package com.savolodya.predictiveportfolio.services;

import com.savolodya.predictiveportfolio.models.actiontoken.ActionToken;
import com.savolodya.predictiveportfolio.models.user.Role;
import com.savolodya.predictiveportfolio.models.user.User;
import com.savolodya.predictiveportfolio.models.user.UserRole;
import com.savolodya.predictiveportfolio.models.user.UserStatus;
import com.savolodya.predictiveportfolio.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;
    private final ActionTokenService actionTokenService;
    private final EmailService emailService;

    @Transactional
    public void createRegisterAccountAction(String email) {
        UserRole userRole = userRoleRepository.findByName(Role.ADMIN)
                .orElseThrow();

        // delete user only if it has status PENDING_REGISTER, if user does not activate their account and link is expired
        Optional<User> userOptional = userService.loadUserByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getStatus().equals(UserStatus.PENDING_REGISTER))
                return;

            userService.deleteAndFlush(user);
        }

        User user = userService.save(new User(email, null, List.of(userRole), UserStatus.PENDING_REGISTER));
        ActionToken accountRegisterToken = actionTokenService.createOrResetRegisterAccountToken(user);
        emailService.sendRegisterAccountEmail(email, accountRegisterToken.getToken());
    }
}
