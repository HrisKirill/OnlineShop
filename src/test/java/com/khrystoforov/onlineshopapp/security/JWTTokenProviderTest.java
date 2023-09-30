package com.khrystoforov.onlineshopapp.security;

import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JWTTokenProviderTest {
    @Mock
    private Authentication authentication;
    private final String secret = "yourSecretKey";
    private final Long expirationTime = 3600000L;

    private final JWTTokenProvider jwtTokenProvider = new JWTTokenProvider(secret, expirationTime);

    private User getTestUser() {
        return User.builder()
                .id(1L)
                .email("test@email.com")
                .name("test")
                .role(Role.CLIENT)
                .build();
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateAndValidateToken() {
        when(authentication.getPrincipal()).thenReturn(getTestUser());

        String token = jwtTokenProvider.generateToken(authentication);

        assertTrue(jwtTokenProvider.validateToken(token));

        assertFalse(jwtTokenProvider.validateToken("invalidToken"));
    }

    @Test
    public void testParseToken() {
        when(authentication.getPrincipal()).thenReturn(getTestUser());

        String token = jwtTokenProvider.generateToken(authentication);
        User parsedUser = jwtTokenProvider.parseToken(token);

        assertNotNull(parsedUser);
        assertEquals(getTestUser().getId(), parsedUser.getId());
        assertEquals(getTestUser().getEmail(), parsedUser.getEmail());
        assertEquals(getTestUser().getName(), parsedUser.getName());
        assertEquals(getTestUser().getRole(), parsedUser.getRole());

        assertNull(jwtTokenProvider.parseToken("invalidToken"));
    }
}
