package com.savolodya.predictiveportfolio.services;

import com.savolodya.predictiveportfolio.exceptions.UserNotFoundException;
import com.savolodya.predictiveportfolio.models.user.User;
import com.savolodya.predictiveportfolio.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    public Optional<User> loadUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteAndFlush(User user) {
        userRepository.delete(user);
        userRepository.flush();
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
