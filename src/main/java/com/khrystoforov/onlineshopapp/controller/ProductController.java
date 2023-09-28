package com.khrystoforov.onlineshopapp.controller;

import com.khrystoforov.onlineshopapp.dto.ProductDTO;
import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.mapper.ProductDTOtoProduct;
import com.khrystoforov.onlineshopapp.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductDTOtoProduct productDTOtoProduct;

    @PostMapping("/manager/add")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO productDTO) {
        Product product = productDTOtoProduct.convertProductDTOToProduct(productDTO);
        return ResponseEntity.ok(productService.create(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productService.findAllProducts());
    }
}
