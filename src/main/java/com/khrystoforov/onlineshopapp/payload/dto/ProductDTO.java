package com.khrystoforov.onlineshopapp.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank(message = "Product price is required")
    private Double price;
    @NotBlank(message = "Product quantity is required")
    private Integer quantity;
}
