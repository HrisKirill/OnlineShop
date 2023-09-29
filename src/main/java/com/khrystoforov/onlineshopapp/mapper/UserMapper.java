package com.khrystoforov.onlineshopapp.mapper;

import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.payload.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public final class UserMapper {

    public UserDTO convertUserToDTO(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
