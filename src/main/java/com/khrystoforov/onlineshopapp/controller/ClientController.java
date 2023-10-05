package com.khrystoforov.onlineshopapp.controller;

import com.khrystoforov.onlineshopapp.payload.dto.OrderDTO;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.security.SecurityConstants;
import com.khrystoforov.onlineshopapp.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SecurityConstants.CLIENT_URLS)
@AllArgsConstructor
public class ClientController {

    private final OrderService orderService;

    @PostMapping("/products/{productName}/quantity/{quantity}")
    public ResponseEntity<MessageResponse> addProductsToOrder(
            @PathVariable String productName,
            @PathVariable Integer quantity) {
        return ResponseEntity.ok(orderService.addProductsToOrder(productName, quantity));
    }

    @GetMapping("/order")
    public ResponseEntity<OrderDTO> getAllProductsInOrder() {
        return ResponseEntity.ok(orderService.getUserOrder());
    }

    @DeleteMapping("/payment")
    public ResponseEntity<MessageResponse> paymentForOrder() {
        return ResponseEntity.ok(orderService.paymentForOrder());
    }
}
