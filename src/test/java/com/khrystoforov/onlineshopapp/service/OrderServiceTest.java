package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import com.khrystoforov.onlineshopapp.entity.enums.ProductStatus;
import com.khrystoforov.onlineshopapp.entity.enums.Role;
import com.khrystoforov.onlineshopapp.exception.OrderNotFoundException;
import com.khrystoforov.onlineshopapp.exception.ProductNotFoundException;
import com.khrystoforov.onlineshopapp.exception.ProductQuantityException;
import com.khrystoforov.onlineshopapp.mapper.OrderMapper;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;
    @InjectMocks
    private OrderService orderService;

    private Order getTestOrder() {
        return new Order(getTestUser());
    }

    private User getTestUser() {
        return User.builder()
                .id(1L)
                .name("test")
                .email("test")
                .password("test")
                .role(Role.CLIENT)
                .build();
    }

    private Product getTestProduct() {
        return Product.builder()
                .id(1L)
                .name("test")
                .status(ProductStatus.FREE)
                .price(500.)
                .build();
    }

    @Test
    void testGetUserOrder() {
        when(userService.getCurrentUser())
                .thenReturn(getTestUser());

        when(orderRepository.findOrderByOwnerId(1L))
                .thenReturn(Optional.of(getTestOrder()));

        assertEquals(orderMapper.orderToOrderDTO(getTestOrder(), getTestUser()),
                orderService.getUserOrder());
    }

    @Test
    void testGetUserOrderNotFoundException() {
        when(userService.getCurrentUser())
                .thenReturn(getTestUser());

        when(orderRepository.findOrderByOwnerId(1L))
                .thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.getUserOrder());
    }

    @Test
    void testGetByOwner() {
        when(userService.getCurrentUser())
                .thenReturn(getTestUser());

        when(orderRepository.findOrderByOwner(getTestUser()))
                .thenReturn(Optional.of(getTestOrder()));

        assertEquals(getTestOrder(), orderService.getOrderOrCreateIfNotExistByOwner());
    }

    @Test
    void testPaymentForOrder() {
        when(userService.getCurrentUser())
                .thenReturn(getTestUser());

        when(orderRepository.findOrderByOwner(getTestUser()))
                .thenReturn(Optional.of(getTestOrder()));


        assertEquals(new MessageResponse("Payment was successful"),
                orderService.paymentForOrder());
    }

    @Test
    void testPaymentForOrderNotFoundException() {
        when(userService.getCurrentUser()).thenReturn(getTestUser());
        when(orderRepository.findOrderByOwner(getTestUser()))
                .thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.paymentForOrder());
    }


    @Test
    void testQuantityIsBelowZero() {
        assertThrows(IllegalArgumentException.class,
                () -> orderService.addProductsToOrder(getTestProduct().getName(), -10));
        assertThrows(IllegalArgumentException.class,
                () -> orderService.addProductsToOrder(getTestProduct().getName(), 0));
    }

    @Test
    void testAddProductsToOrder() {
        int quantity = 1;
        when(userService.getCurrentUser()).thenReturn(getTestUser());
        when(productService
                .findAllProductsByNameAndStatus(getTestProduct().getName(), ProductStatus.FREE))
                .thenReturn(List.of(getTestProduct()));

        when(orderRepository.save(any(Order.class))).thenReturn(getTestOrder());

        assertEquals(new MessageResponse("Products add to order successfully"),
                orderService.addProductsToOrder(getTestProduct().getName(), quantity));

        productService.updateProductStatus(ProductStatus.IN_PROCESSING, getTestProduct().getId());
    }


    @Test
    void testAddProductsToOrderIfProductsNotExist() {
        int quantity = 1;
        when(productService
                .findAllProductsByNameAndStatus(getTestProduct().getName(), ProductStatus.FREE))
                .thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () ->
                orderService.addProductsToOrder(getTestProduct().getName(), quantity));
    }


    @Test
    void testAddProductsToOrderIfQuantityMoreThenProducts() {
        int quantity = 2;
        when(productService
                .findAllProductsByNameAndStatus(getTestProduct().getName(), ProductStatus.FREE))
                .thenReturn(List.of(getTestProduct()));

        assertThrows(ProductQuantityException.class, () ->
                orderService.addProductsToOrder(getTestProduct().getName(), quantity));
    }

    @Test
    void testDeleteUnpaidOrders() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        List<Order> unpaidOrders = new ArrayList<>();
        unpaidOrders.add(getTestOrder());

        when(orderRepository.findByOrderStatusAndDateCreatedBefore(
                eq(OrderStatus.UNPAID),
                ArgumentMatchers.argThat(
                        date -> date.isAfter(tenMinutesAgo.minusSeconds(1)) &&
                                date.isBefore(tenMinutesAgo.plusSeconds(1))
                )))
                .thenReturn(unpaidOrders);

        orderService.deleteUnpaidOrders();

        for (Order order : unpaidOrders) {
            verify(orderRepository, times(1))
                    .updateProductStatusesToFreeInOrder(order.getId());
        }

        verify(orderRepository, times(1)).deleteAll(unpaidOrders);
    }
}