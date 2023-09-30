package com.khrystoforov.onlineshopapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.mapper.ProductMapper;
import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ManagerControllerTest {
    MockMvc mockMvc;
    @Mock
    private ProductService productService;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ManagerController controller;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    public void testAddProducts() throws Exception {
        ProductDTO productDTO =ProductDTO
                .builder()
                .name("test")
                .price(300.0)
                .quantity(2)
                .build();

        when(productMapper.convertProductDTOToProduct(productDTO)).thenReturn(new Product());
        when(productService.addProducts(any(), eq(2))).thenReturn(productDTO);

        mockMvc.perform(post("/manager/add")
                .content(asJsonString(productDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.price").value(300.0))
                .andExpect(jsonPath("$.quantity").value(2));


        verify(productMapper, times(1)).convertProductDTOToProduct(productDTO);
        verify(productService, times(1)).addProducts(any(), eq(2));
        verifyNoMoreInteractions(productMapper, productService);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
