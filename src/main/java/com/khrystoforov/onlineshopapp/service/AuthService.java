package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.Role;
import com.khrystoforov.onlineshopapp.exception.UserExistException;
import com.khrystoforov.onlineshopapp.payload.request.LoginRequest;
import com.khrystoforov.onlineshopapp.payload.request.RegisterRequest;
import com.khrystoforov.onlineshopapp.payload.response.JWTTokenSuccessResponse;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.security.JWTTokenProvider;
import com.khrystoforov.onlineshopapp.security.SecurityConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;


    public JWTTokenSuccessResponse login(LoginRequest loginRequest) {
//        User user = userService.findUserByEmail(loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        log.info("logging with [{}]", authentication.getPrincipal());
        return new JWTTokenSuccessResponse(true, jwt);
    }

    public MessageResponse register(RegisterRequest registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            throw new UserExistException("Email is already exist!");
        }

        User parent = User.builder()
                .email(registerRequest.getEmail())
                .name(registerRequest.getUsername())
                .role(Role.getRole(registerRequest.getRole()))
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userService.create(parent);

        return new MessageResponse("User register successfully");
    }


}