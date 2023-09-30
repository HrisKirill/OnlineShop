package com.khrystoforov.onlineshopapp.service;


import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.entity.enums.ProductStatus;
import com.khrystoforov.onlineshopapp.mapper.ProductMapper;
import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
import com.khrystoforov.onlineshopapp.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAllProductsByNameAndStatus(String name, ProductStatus productStatus) {
        return productRepository.findAllByNameAndStatus(name, productStatus);
    }

    public List<ProductDTO> findFreeProducts() {
        Map<String, List<Product>> collect = productRepository.findAll().stream()
                .filter(p -> ProductStatus.FREE.name().equalsIgnoreCase(p.getStatus().name()))
                .collect(Collectors.groupingBy(Product::getName));

        return collect.values().stream()
                .map(products -> productMapper.convertProductToProductDTO(
                        products.get(0), products.size()))
                .collect(Collectors.toList());
    }

    public ProductDTO addProducts(Product product, Integer count) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            products.add(Product.builder()
                    .name(product.getName())
                    .price(product.getPrice())
                    .status(ProductStatus.FREE)
                    .build()
            );
        }
        productRepository.saveAll(products);

        return productMapper.convertProductToProductDTO(product, count);
    }

    public void updateProductStatus(ProductStatus productStatus, Long productId) {
        productRepository.updateProductStatusById(productStatus, productId);
    }
}
