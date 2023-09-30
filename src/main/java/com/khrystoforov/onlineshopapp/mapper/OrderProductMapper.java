package com.khrystoforov.onlineshopapp.mapper;

import com.khrystoforov.onlineshopapp.entity.OrderProduct;
import com.khrystoforov.onlineshopapp.payload.dto.OrderProductDTO;
import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public  class OrderProductMapper {

    public OrderProductDTO ConvertOrderProductToDTO(OrderProduct orderProduct) {
        return new OrderProductDTO(
                ProductDTO.builder()
                        .name(orderProduct.getProduct().getName())
                        .price(orderProduct.getProduct().getPrice())
                        .build()
        );
    }

}
