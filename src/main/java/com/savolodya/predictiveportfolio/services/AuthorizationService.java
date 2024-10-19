package com.savolodya.predictiveportfolio.services;

import com.savolodya.predictiveportfolio.exceptions.ActionTokenNotFoundException;
import com.savolodya.predictiveportfolio.models.actiontoken.ActionToken;
import com.savolodya.predictiveportfolio.models.actiontoken.ActionTokenType;
import com.savolodya.predictiveportfolio.models.team.Team;
import com.savolodya.predictiveportfolio.models.user.*;
import com.savolodya.predictiveportfolio.repositories.UserRoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {

    private final AuthenticationManager authenticationManager;
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;
    private final TeamService teamService;
    private final ActionTokenService actionTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createRegisterUserAction(String email, String teamName) {
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

        Team team = teamService.findOrCreateTeam(teamName);
        User user = userService.save(new User(email, null, UserStatus.PENDING_REGISTER));
        user.setTeams(Set.of(userService.save(new UserTeam(user, team, Set.of(userRole)))));
        userService.save(user);

        ActionToken accountRegisterToken = actionTokenService.createOrResetRegisterAccountToken(user);
        emailService.sendRegisterAccountEmail(email, accountRegisterToken.getToken());

        log.info("Account [{}] started registration", user.getUuid());
    }

    @Transactional
    public void finishRegisterUserAction(UUID registerActionToken, String password) {
        ActionToken actionToken = actionTokenService.findByTokenAndTypeAndExpiryTimestampAfterNow(registerActionToken, ActionTokenType.USER_REGISTER)
                .orElseThrow(() -> new ActionTokenNotFoundException(ActionTokenType.USER_REGISTER, registerActionToken));
        User user = actionToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(UserStatus.ACTIVE);

        actionTokenService.delete(actionToken);
        emailService.sendRegisterAccountConfirmationEmail(user.getEmail()); // TODO: Take out from transaction, because if will be errors in commiting transaction email should not be send.

        log.info("Account [{}] activated", user.getUuid());
    }

    public void authorizeAccount(String email, String password) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        request.getSession().invalidate();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(auth);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Create session if not found
        HttpSession session = request.getSession(true);
        // Manually save SecurityContext to session repository
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

}
