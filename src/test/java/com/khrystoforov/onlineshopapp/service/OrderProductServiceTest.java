package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.OrderProduct;
import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import com.khrystoforov.onlineshopapp.entity.enums.ProductStatus;
import com.khrystoforov.onlineshopapp.exception.ProductNotFoundException;
import com.khrystoforov.onlineshopapp.exception.ProductQuantityException;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.repository.OrderProductRepository;
import org.aspectj.bridge.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProductServiceTest {

    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private OrderService orderService;
    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderProductService orderProductService;

    private Product getTestProduct() {
        return Product.builder()
                .id(1L)
                .name("test")
                .status(ProductStatus.FREE)
                .price(500.)
                .build();
    }

    private Order getTestOrder() {
        return Order.builder()
                .dateCreated(LocalDateTime.of(2002, Month.APRIL, 10, 10, 10, 10))
                .orderStatus(OrderStatus.UNPAID)
                .owner(new User())
                .build();
    }


    @Test
    void testQuantityIsBelowZero() {
        assertThrows(IllegalArgumentException.class,
                () -> orderProductService.addProductsToOrder(getTestProduct().getName(), -10));
        assertThrows(IllegalArgumentException.class,
                () -> orderProductService.addProductsToOrder(getTestProduct().getName(), 0));
    }

    @Test
    void testAddProductsToOrder() {
        int quantity = 1;
        when(orderService.getOrderOrCreateIfNotExistByOwner())
                .thenReturn(getTestOrder());

        when(productService
                .findAllProductsByNameAndStatus(getTestProduct().getName(), ProductStatus.FREE))
                .thenReturn(List.of(getTestProduct()));

        assertEquals(new MessageResponse("Products add to order successfully"),
                orderProductService.addProductsToOrder(getTestProduct().getName(), quantity));
        verify(productService, times(1)).updateProductStatus(
                eq(ProductStatus.IN_PROCESSING), anyLong());
        verify(orderProductRepository, times(quantity)).save(any(OrderProduct.class));
    }

    @Test
    void testAddProductsToOrderIfProductsNotExist() {
        int quantity = 1;
        when(orderService.getOrderOrCreateIfNotExistByOwner())
                .thenReturn(getTestOrder());

        when(productService
                .findAllProductsByNameAndStatus(getTestProduct().getName(), ProductStatus.FREE))
                .thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class,()->
                orderProductService.addProductsToOrder(getTestProduct().getName(), quantity));
    }

    @Test
    void testAddProductsToOrderIfQuantityMoreThenProducts() {
        int quantity = 2;
        when(orderService.getOrderOrCreateIfNotExistByOwner())
                .thenReturn(getTestOrder());

        when(productService
                .findAllProductsByNameAndStatus(getTestProduct().getName(), ProductStatus.FREE))
                .thenReturn(List.of(getTestProduct()));

        assertThrows(ProductQuantityException.class,()->
                orderProductService.addProductsToOrder(getTestProduct().getName(), quantity));
    }
}