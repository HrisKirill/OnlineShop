package com.khrystoforov.onlineshopapp.service;


import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.exception.ProductNotFoundException;
import com.khrystoforov.onlineshopapp.repository.ProductRepository;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAllProductsByName(String name) {
        return productRepository.findAllByName(name);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product is not exist with id " + id));
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
