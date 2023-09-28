package com.khrystoforov.onlineshopapp.controller;


import com.khrystoforov.onlineshopapp.payload.request.LoginRequest;
import com.khrystoforov.onlineshopapp.payload.request.RegisterRequest;
import com.khrystoforov.onlineshopapp.payload.response.JWTTokenSuccessResponse;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping({"/register", "/sign-up"})
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping({"/login", "sign-in"})
    public ResponseEntity<JWTTokenSuccessResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

}
