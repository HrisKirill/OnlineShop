package com.khrystoforov.onlineshopapp.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterRequest {
    private String email;
    private String username;
    private String role;
    private String password;
}
