package com.savolodya.predictiveportfolio.repositories;

import com.savolodya.predictiveportfolio.models.user.Role;
import com.savolodya.predictiveportfolio.models.user.UserRole;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(value = false)
class UserRoleRepositoryTests {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    @Order(1)
    void should_CreateUserRole() {
        // given
        UserRole role = UserRole.builder()
                .name(Role.ADMIN)
                .build();

        // when
        userRoleRepository.save(role);

        // then
        assertThat(role.getId()).isPositive();
    }

    @Test
    @Order(2)
    void should_FindUserRole() {
        // when
        UserRole role = userRoleRepository.findById(1L).get();

        // then
        assertThat(role.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    void should_FindUserRoleByName() {
        // when
        UserRole role = userRoleRepository.findByName(Role.ADMIN).get();

        // then
        assertThat(role.getName()).isEqualTo(Role.ADMIN);
    }

    @Test
    @Order(4)
    void should_FindAllUsers() {
        // when
        List<UserRole> userRoles = userRoleRepository.findAll();

        // hen
        assertThat(userRoles).isNotEmpty();
    }

    @Test
    @Order(5)
    void should_DeleteUserRole() {
        // when
        userRoleRepository.deleteById(1L);
        Optional<UserRole> userRoleOptional = userRoleRepository.findById(1L);

        // then
        assertThat(userRoleOptional).isEmpty();
    }

}