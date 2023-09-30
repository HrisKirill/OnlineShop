package com.khrystoforov.onlineshopapp.controller;

import com.khrystoforov.onlineshopapp.mapper.ProductMapper;
import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
import com.khrystoforov.onlineshopapp.security.SecurityConstants;
import com.khrystoforov.onlineshopapp.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SecurityConstants.MANAGER_URLS)
@AllArgsConstructor
public class ManagerController {
    private final ProductService productService;

    private final ProductMapper productMapper;

    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProducts(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.addProducts(
                productMapper.convertProductDTOToProduct(productDTO), productDTO.getQuantity()));
    }
}
