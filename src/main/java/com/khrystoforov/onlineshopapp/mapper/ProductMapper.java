package com.khrystoforov.onlineshopapp.mapper;

import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
import com.khrystoforov.onlineshopapp.entity.Product;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO productToProductDTO(Product product, Integer quantity);

    Product productDTOToProduct(ProductDTO productDTO);
}
