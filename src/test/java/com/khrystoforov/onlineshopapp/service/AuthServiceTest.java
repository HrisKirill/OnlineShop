package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.Role;
import com.khrystoforov.onlineshopapp.exception.UserExistException;
import com.khrystoforov.onlineshopapp.payload.request.LoginRequest;
import com.khrystoforov.onlineshopapp.payload.request.RegisterRequest;
import com.khrystoforov.onlineshopapp.payload.response.JWTTokenSuccessResponse;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.security.JWTTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private  AuthenticationManager authenticationManager;
    @Mock
    private  UserService userService;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  JWTTokenProvider jwtTokenProvider;
    @InjectMocks
    private AuthService authService;

    private RegisterRequest getTestRegisterRequest(){
        return RegisterRequest.builder()
                .email("test@email.com")
                .username("test")
                .password("test")
                .role(Role.CLIENT.name())
                .build();
    }

    private LoginRequest getTestLoginRequest(){
        return LoginRequest.builder()
                .email("test@email.com")
                .password("test")
                .build();
    }
    @Test
    void testRegister() {
            User user = User.builder()
                .email("test@email.com")
                .name("test")
                .role(Role.CLIENT)
                .password("encoded_password")
                .build();

        when(userService.existsByEmail(getTestRegisterRequest().getEmail())).thenReturn(false);
        when(passwordEncoder.encode(getTestRegisterRequest().getPassword())).thenReturn(user.getEmail());
        when(userService.create(any(User.class))).thenReturn(user);


        MessageResponse response = authService.register(getTestRegisterRequest());

        assertNotNull(response);
        assertEquals("User register successfully", response.getMessage());
    }

    @Test
    void testRegisterUserExists() {
        when(userService.existsByEmail(getTestRegisterRequest().getEmail())).thenReturn(true);
        assertThrows(UserExistException.class, () -> authService.register(getTestRegisterRequest()));
    }

    @Test
    void testLogin() {
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("mocked_jwt");

        JWTTokenSuccessResponse response = authService.login(getTestLoginRequest());

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Bearer mocked_jwt", response.getToken());
    }
}