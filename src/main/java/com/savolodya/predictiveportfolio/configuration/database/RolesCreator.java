package com.savolodya.predictiveportfolio.configuration.database;

import com.savolodya.predictiveportfolio.models.user.Role;
import com.savolodya.predictiveportfolio.models.user.UserRole;
import com.savolodya.predictiveportfolio.repositories.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class RolesCreator {
    private final UserRoleRepository roleRepository;

    public void create() {
        createRoleIfNotFound(Role.ADMIN);
        createRoleIfNotFound(Role.USER);
    }

    @Transactional
    protected void createRoleIfNotFound(Role role) {
        UserRole userRole = roleRepository.findByName(role)
                .orElse(null);

        if (userRole == null) {
            log.info("Creating role {}", role.getKey());
            roleRepository.save(new UserRole(role));
        }
    }
}
