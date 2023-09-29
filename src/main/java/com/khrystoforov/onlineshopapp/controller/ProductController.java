package com.khrystoforov.onlineshopapp.controller;

import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
import com.khrystoforov.onlineshopapp.mapper.ProductMapper;
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

    private final ProductMapper productMapper;

    @PostMapping("/manager/add")
    public ResponseEntity<ProductDTO> addProducts(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.addProducts(
                productMapper.convertProductDTOToProduct(productDTO), productDTO.getQuantity()));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findFreeProducts() {
        return ResponseEntity.ok(productService.findFreeProducts());
    }
}
