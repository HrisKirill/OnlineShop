package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import com.khrystoforov.onlineshopapp.exception.OrderNotFoundException;
import com.khrystoforov.onlineshopapp.mapper.OrderMapper;
import com.khrystoforov.onlineshopapp.payload.dto.OrderDTO;
import com.khrystoforov.onlineshopapp.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductService productService;

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + id));
        return orderMapper.convertOrderToDTO(order);
    }

    public Order getOrderByUser(User owner) {
        Optional<Order> optionalOrder = orderRepository
                .findOrderByOrderStatusUnpaidAndOwner(owner);
        return optionalOrder.orElseGet(() -> orderRepository.save(Order.builder()
                .dateCreated(LocalDateTime.now())
                .orderStatus(OrderStatus.UNPAID)
                .owner(owner)
                .build()));
    }

    @Scheduled(fixedRate = 30_000)
    public void deleteUnpaidOrders() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        List<Order> unpaidOrders = orderRepository
                .findByOrderStatusAndDateCreatedBefore(OrderStatus.UNPAID, tenMinutesAgo);
        orderRepository.deleteAll(unpaidOrders);
    }
}
