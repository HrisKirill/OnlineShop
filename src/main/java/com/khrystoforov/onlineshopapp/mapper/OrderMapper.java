package com.khrystoforov.onlineshopapp.mapper;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.payload.dto.OrderDTO;
import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface OrderMapper {
    default OrderDTO orderToOrderDTO(Order order, User user) {
        return OrderDTO.builder()
                .dateCreated(order.getDateCreated())
                .orderStatus(order.getOrderStatus())
                .owner(UserMapper.INSTANCE.userToUserDTO(user))
                .products(order.getProducts().entrySet().stream()
                        .map(e -> ProductDTO.builder()
                                .name(e.getKey().getName())
                                .price(e.getKey().getPrice())
                                .quantity(e.getValue())
                                .build())
                        .collect(Collectors.toList())
                )
                .totalOrderPrice(order.getTotalOrderPrice())
                .build();
    }

}
