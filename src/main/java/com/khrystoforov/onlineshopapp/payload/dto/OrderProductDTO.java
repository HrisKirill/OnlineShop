package com.khrystoforov.onlineshopapp.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderProductDTO {
    private ProductDTO productDTO;
    private Double totalPrice;
}
