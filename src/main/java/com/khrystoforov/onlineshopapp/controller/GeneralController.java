package com.khrystoforov.onlineshopapp.controller;

import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
import com.khrystoforov.onlineshopapp.security.SecurityConstants;
import com.khrystoforov.onlineshopapp.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(SecurityConstants.GENERAL_URLS)
@AllArgsConstructor
public class GeneralController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> findFreeProducts() {
        return ResponseEntity.ok(productService.findFreeProducts());
    }
}
