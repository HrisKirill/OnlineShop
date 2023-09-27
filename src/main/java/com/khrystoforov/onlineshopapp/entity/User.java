package com.khrystoforov.onlineshopapp.entity;


import com.khrystoforov.onlineshopapp.entity.enums.Role;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Name is required.")
    private String name;
    private Role role;
    @OneToMany
    private List<Order> orders;
}
