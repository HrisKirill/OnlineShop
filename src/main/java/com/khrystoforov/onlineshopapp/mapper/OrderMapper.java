package com.khrystoforov.onlineshopapp.mapper;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.OrderProduct;
import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.payload.dto.OrderDTO;
import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public final class OrderMapper {

    private final UserMapper userMapper;
    private final OrderProductMapper orderProductMapper;

    public OrderDTO convertOrderToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .dateCreated(order.getDateCreated())
                .owner(userMapper.convertUserToDTO(order.getOwner()))
                .products(order.getOrderProducts().stream()
                        .map(orderProductMapper::ConvertOrderProductToDTO)
                        .collect(Collectors.toList()))
                .totalOrderPrice(order.getTotalOrderPrice())
                .build();
    }
}
