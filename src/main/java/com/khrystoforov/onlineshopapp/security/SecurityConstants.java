package com.khrystoforov.onlineshopapp.security;

public final class SecurityConstants {
    private SecurityConstants() {}
    public static final String AUTH_URLS = "/api/auth/*";
    public static final String MANAGER_URLS = "/api/products/manager/*";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
