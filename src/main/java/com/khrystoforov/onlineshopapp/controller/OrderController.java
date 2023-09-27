package com.khrystoforov.onlineshopapp.controller;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.OrderProduct;
import com.khrystoforov.onlineshopapp.service.OrderProductService;
import com.khrystoforov.onlineshopapp.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderProductService orderProductService;

    @PostMapping("/{orderId}/products/{productName}/quantity/{quantity}")
    public ResponseEntity<OrderProduct> addProductsToOrder(
            @PathVariable Long orderId,
            @PathVariable String productName,
            @PathVariable Integer quantity) {
        return ResponseEntity.ok(orderProductService.addProductsToOrder(orderId, productName, quantity));
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getAllOrderProducts(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }


}
