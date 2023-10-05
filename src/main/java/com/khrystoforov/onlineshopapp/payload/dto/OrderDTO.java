package com.khrystoforov.onlineshopapp.payload.dto;

import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Data
public class OrderDTO {
    private LocalDateTime dateCreated;
    private OrderStatus orderStatus;
    private UserDTO owner;
    private List<ProductDTO> products;
    private Double totalOrderPrice;
}
