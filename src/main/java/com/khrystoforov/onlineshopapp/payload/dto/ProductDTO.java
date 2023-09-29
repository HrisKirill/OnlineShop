package com.khrystoforov.onlineshopapp.payload.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
    private String name;
    private Double price;
    private Integer quantity;
}
