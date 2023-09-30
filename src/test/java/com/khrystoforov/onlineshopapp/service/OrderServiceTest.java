package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import com.khrystoforov.onlineshopapp.exception.OrderNotFoundException;
import com.khrystoforov.onlineshopapp.mapper.OrderMapper;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private OrderService orderService;

    private Order getTestOrder() {
        return Order.builder()
                .dateCreated(LocalDateTime.of(2002, Month.APRIL, 10, 10, 10, 10))
                .orderStatus(OrderStatus.UNPAID)
                .owner(new User())
                .build();
    }

    private User getTestUser() {
        return User.builder()
                .id(getTestOrder().getId())
                .build();
    }

    @Test
    void testGetUserOrder() {
        when(userService.getCurrentUser())
                .thenReturn(getTestUser());

        when(orderRepository.findOrderByOwnerId(getTestOrder().getId()))
                .thenReturn(Optional.of(getTestOrder()));

        assertEquals(orderMapper.convertOrderToDTO(getTestOrder()),
                orderService.getUserOrder());
    }

    @Test
    void testGetUserOrderNotFoundException() {
        when(userService.getCurrentUser())
                .thenReturn(getTestUser());

        when(orderRepository.findOrderByOwnerId(getTestOrder().getId()))
                .thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.getUserOrder());
    }

    @Test
    void testGetByOwner() {
        ;

        when(userService.getCurrentUser())
                .thenReturn(getTestUser());

        when(orderRepository.findOrderByOrderStatusUnpaidAndOwner(getTestUser()))
                .thenReturn(Optional.of(getTestOrder()));

        assertEquals(getTestOrder(), orderService.getOrderOrCreateIfNotExistByOwner());
    }

    @Test
    void testPaymentForOrder() {
        when(userService.getCurrentUser())
                .thenReturn(getTestUser());

        when(orderRepository.findOrderByOrderStatusUnpaidAndOwner(getTestUser()))
                .thenReturn(Optional.of(getTestOrder()));

        assertEquals(new MessageResponse("Payment was successful"),
                orderService.paymentForOrder());
    }

    @Test
    void testPaymentForOrderNotFoundException() {
        when(userService.getCurrentUser()).thenReturn(getTestUser());
        when(orderRepository.findOrderByOrderStatusUnpaidAndOwner(getTestUser()))
                .thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.paymentForOrder());
    }

}