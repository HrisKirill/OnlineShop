package com.khrystoforov.onlineshopapp.payload.request;

import lombok.Getter;

@Getter
public class RegisterRequest {
    private String email;
    private String username;
    private String role;
    private String password;
}
