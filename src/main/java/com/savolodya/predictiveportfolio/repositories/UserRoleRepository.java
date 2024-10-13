package com.savolodya.predictiveportfolio.repositories;

import com.savolodya.predictiveportfolio.models.user.Role;
import com.savolodya.predictiveportfolio.models.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(Role name);
}
