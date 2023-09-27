package com.khrystoforov.onlineshopapp.mapper;

import com.khrystoforov.onlineshopapp.dto.ProductDTO;
import com.khrystoforov.onlineshopapp.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOtoProduct {

    public Product convertProductDTOToProduct(ProductDTO productDTO) {
        return Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .build();
    }
}
