package com.khrystoforov.onlineshopapp.controller;

import com.khrystoforov.onlineshopapp.payload.dto.OrderDTO;
import com.khrystoforov.onlineshopapp.payload.dto.OrderProductDTO;
import com.khrystoforov.onlineshopapp.service.OrderProductService;
import com.khrystoforov.onlineshopapp.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/order")
@Validated
public class OrderController {
    private final OrderService orderService;
    private final OrderProductService orderProductService;

    @PostMapping("/products/{productName}/quantity/{quantity}")
    public ResponseEntity<OrderProductDTO> addProductsToOrder(
            @PathVariable String productName,
            @PathVariable Integer quantity) {
        return ResponseEntity.ok(orderProductService.addProductsToOrder(productName, quantity));
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getAllOrderProducts(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }


}
