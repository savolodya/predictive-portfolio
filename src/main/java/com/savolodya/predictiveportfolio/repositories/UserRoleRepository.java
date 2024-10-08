package com.savolodya.predictiveportfolio.repositories;

import com.savolodya.predictiveportfolio.models.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
