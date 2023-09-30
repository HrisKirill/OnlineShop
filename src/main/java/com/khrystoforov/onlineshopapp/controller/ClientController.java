package com.khrystoforov.onlineshopapp.controller;

import com.khrystoforov.onlineshopapp.payload.dto.OrderDTO;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.security.SecurityConstants;
import com.khrystoforov.onlineshopapp.service.OrderProductService;
import com.khrystoforov.onlineshopapp.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SecurityConstants.CLIENT_URLS)
@AllArgsConstructor
public class ClientController {

    private final OrderService orderService;
    private final OrderProductService orderProductService;

    @PostMapping("/products/{productName}/quantity/{quantity}")
    public ResponseEntity<MessageResponse> addProductsToOrder(
            @PathVariable String productName,
            @PathVariable Integer quantity) {
        return ResponseEntity.ok(orderProductService.addProductsToOrder(productName, quantity));
    }

    @GetMapping
    public ResponseEntity<OrderDTO> getAllProductsInOrder() {
        return ResponseEntity.ok(orderService.getUserOrder());
    }

    @DeleteMapping("/payment")
    public ResponseEntity<MessageResponse> paymentForOrder() {
        return ResponseEntity.ok(orderService.paymentForOrder());
    }
}
