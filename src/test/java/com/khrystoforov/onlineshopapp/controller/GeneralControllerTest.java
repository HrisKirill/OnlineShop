package com.khrystoforov.onlineshopapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
import com.khrystoforov.onlineshopapp.security.SecurityConstants;
import com.khrystoforov.onlineshopapp.service.ProductService;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GeneralControllerTest {
    private MockMvc mockMvc;
    @Mock
    private ProductService productService;

    @InjectMocks
    private GeneralController controller;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void testFindFreeProducts() throws Exception {
        List<ProductDTO> productDTOList = List.of(
                ProductDTO.builder()
                        .name("test1")
                        .quantity(2)
                        .price(300.0)
                        .build()
        );

        when(productService.findFreeProducts()).thenReturn(productDTOList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(SecurityConstants.GENERAL_URLS + "/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("test1"))
                .andExpect(jsonPath("$[0].price").value(300.0))
                .andExpect(jsonPath("$[0].quantity").value(2));

        verify(productService, times(1)).findFreeProducts();
    }
}