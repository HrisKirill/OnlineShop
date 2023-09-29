package com.khrystoforov.onlineshopapp.payload.dto;

import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime dateCreated;
    private OrderStatus orderStatus;
    private UserDTO owner;
    private List<OrderProductDTO> products;
    private Double totalOrderPrice;
}
