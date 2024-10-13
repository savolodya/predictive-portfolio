package com.savolodya.predictiveportfolio.repositories;

import com.savolodya.predictiveportfolio.models.user.User;
import com.savolodya.predictiveportfolio.models.user.UserStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(value = false)
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void should_CreateUser() {
        // given
        User user = User.builder()
                .email("test@test.com")
                .password("aA1!aA1!")
                .uuid(UUID.randomUUID())
                .status(UserStatus.ACTIVE)
                .build();

        // when
        userRepository.save(user);

        // then
        assertThat(user.getId()).isPositive();
    }

    @Test
    @Order(2)
    void should_FindUserByEmail() {
        // when
        User user = userRepository.findByEmail("test@test.com").get();

        // then
        assertThat(user.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @Order(3)
    void should_FindAllUsers() {
        // when
        List<User> users = userRepository.findAll();

        // hen
        assertThat(users).isNotEmpty();
    }

    @Test
    @Order(4)
    void should_UpdateUserEmail() {
        // when
        User user = userRepository.findByEmail("test@test.com").get();
        user.setEmail("test2@test.com");
        User updatedUser = userRepository.save(user);

        // then
        assertThat(updatedUser.getEmail()).isEqualTo("test2@test.com");
    }

    @Test
    @Order(5)
    void should_DeleteUser() {
        // when
        User user = userRepository.findByEmail("test2@test.com").get();
        userRepository.delete(user);
        Optional<User> userOptional = userRepository.findByEmail("test2@test.com");

        // then
        assertThat(userOptional).isEmpty();
    }

}