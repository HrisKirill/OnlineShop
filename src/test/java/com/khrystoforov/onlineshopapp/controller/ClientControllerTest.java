package com.khrystoforov.onlineshopapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import com.khrystoforov.onlineshopapp.payload.dto.OrderDTO;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.security.SecurityConstants;
import com.khrystoforov.onlineshopapp.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private ClientController controller;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void testAddProductsToOrder() throws Exception {
        when(orderService.addProductsToOrder(anyString(), anyInt()))
                .thenReturn(new MessageResponse("Products added successfully"));

        mockMvc.perform(post(SecurityConstants.CLIENT_URLS + "/products/ProductA/quantity/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Products added successfully"));

        verify(orderService, times(1)).addProductsToOrder("ProductA", 5);
    }

    @Test
    void testGetAllProductsInOrder() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder()
                .products(Collections.emptyList())
                .totalOrderPrice(0.)
                .orderStatus(OrderStatus.UNPAID)
                .build();

        when(orderService.getUserOrder()).thenReturn(orderDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(SecurityConstants.CLIENT_URLS + "/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").exists());

        verify(orderService, times(1)).getUserOrder();
    }

    @Test
    void testPaymentForOrder() throws Exception {
        when(orderService.paymentForOrder()).thenReturn(new MessageResponse("Payment successful"));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(SecurityConstants.CLIENT_URLS + "/payment")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Payment successful"));

        verify(orderService, times(1)).paymentForOrder();
    }
}
