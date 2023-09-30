package com.khrystoforov.onlineshopapp.security;

public final class SecurityConstants {
    private SecurityConstants() {
    }

    public static final String AUTH_URLS = "/api/auth/*";
    public static final String MANAGER_URLS = "/manager";
    public static final String CLIENT_URLS = "/client";
    public static final String GENERAL_URLS = "/general";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
