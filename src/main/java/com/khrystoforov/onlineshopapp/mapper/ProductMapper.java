package com.khrystoforov.onlineshopapp.mapper;

import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
import com.khrystoforov.onlineshopapp.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product convertProductDTOToProduct(ProductDTO productDTO) {
        return Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .build();
    }

    public ProductDTO convertProductToProductDTO(Product product, Integer count) {
        return ProductDTO.builder()
                .name(product.getName())
                .price(product.getPrice())
                .quantity(count)
                .build();
    }

}
