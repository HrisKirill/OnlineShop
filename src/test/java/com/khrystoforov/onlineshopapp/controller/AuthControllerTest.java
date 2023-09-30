package com.khrystoforov.onlineshopapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khrystoforov.onlineshopapp.entity.enums.Role;
import com.khrystoforov.onlineshopapp.payload.request.LoginRequest;
import com.khrystoforov.onlineshopapp.payload.request.RegisterRequest;
import com.khrystoforov.onlineshopapp.payload.response.JWTTokenSuccessResponse;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController controller;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    private RegisterRequest getTestRegisterRequest() {
        return RegisterRequest.builder()
                .email("test@email")
                .username("test")
                .password("test")
                .role(Role.MANAGER.name())
                .build();
    }


    @Test
    void testRegister() throws Exception {
        when(authService.register(getTestRegisterRequest())).thenReturn(new MessageResponse("User register successfully"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(getTestRegisterRequest())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(authService, times(1))
                .register(getTestRegisterRequest());
        verifyNoMoreInteractions(authService);
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = LoginRequest
                .builder()
                .password("test")
                .email("test@email.com")
                .build();

        JWTTokenSuccessResponse response =
                new JWTTokenSuccessResponse(true, "Bearer mocked_jwt");

        when(authService.login(loginRequest)).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(response.isSuccess()))
                .andExpect(jsonPath("$.token").value(response.getToken()));

        verify(authService, times(1)).login(loginRequest);
        verifyNoMoreInteractions(authService);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}