package com.khrystoforov.onlineshopapp.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ProductDTO {
    private String name;
    private Double price;
    private Integer quantity;
}