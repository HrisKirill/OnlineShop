package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.Role;
import com.khrystoforov.onlineshopapp.exception.UserNotFoundException;
import com.khrystoforov.onlineshopapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Mock
    private Authentication authentication ;

    @Mock
    private SecurityContext secCont ;

    private User getTestUser() {
        return User.builder()
                .id(1L)
                .email("test@email")
                .name("test")
                .role(Role.MANAGER)
                .build();
    }

    @Test
    void testFindUSerByEmail() {
        when(userRepository.findUserByEmail(getTestUser().getEmail()))
                .thenReturn(Optional.of(getTestUser()));
        assertEquals(getTestUser(), userService.findUserByEmail(getTestUser().getEmail()));
    }

    @Test
    void testFindUserByEmailNotFound() {
        when(userRepository.findUserByEmail(getTestUser().getEmail()))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> userService.findUserByEmail(getTestUser().getEmail()));
    }

    @Test
    void testCreate() {
        when(userRepository.save(getTestUser())).thenReturn(getTestUser());
        assertEquals(getTestUser(), userService.create(getTestUser()));
    }

    @Test
    void testExistsByEmail() {
        when(userRepository.existsUserByEmail(getTestUser().getEmail()))
                .thenReturn(true);
        assertTrue(userService.existsByEmail(getTestUser().getEmail()));
    }

    @Test
    void testNotExistsByEmail() {
        when(userRepository.existsUserByEmail(getTestUser().getEmail()))
                .thenReturn(false);

        assertFalse(userService.existsByEmail(getTestUser().getEmail()));
    }

    @Test
    void testGetCurrentUser(){
        when(secCont.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(secCont);
        when(authentication.getName()).thenReturn(getTestUser().getEmail());
        when(userRepository.findUserByEmail(getTestUser().getEmail()))
                .thenReturn(Optional.of(getTestUser()));

        assertEquals(userService.getCurrentUser(),getTestUser());
    }
}