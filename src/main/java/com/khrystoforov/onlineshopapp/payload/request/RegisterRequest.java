package com.khrystoforov.onlineshopapp.payload.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {
    private String email;
    private String username;
    private String role;
    private String password;
}
