package com.khrystoforov.onlineshopapp.entity.enums;

public enum Role {
    MANAGER, CLIENT;

    Role() {
    }

    public static Role getRole(String name) {
        for (Role role : Role.values()) {
            if (name.equalsIgnoreCase(role.name())) {
                return role;
            }
        }

        return CLIENT;
    }
}
