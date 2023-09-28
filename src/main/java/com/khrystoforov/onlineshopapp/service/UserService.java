package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.exception.UserNotFoundException;
import com.khrystoforov.onlineshopapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not exists"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not exists"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }
}
