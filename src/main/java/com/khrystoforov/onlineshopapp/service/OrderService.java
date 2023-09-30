package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import com.khrystoforov.onlineshopapp.exception.OrderNotFoundException;
import com.khrystoforov.onlineshopapp.mapper.OrderMapper;
import com.khrystoforov.onlineshopapp.payload.dto.OrderDTO;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;

    public OrderDTO getUserOrder() {
        Long userId = userService.getCurrentUser().getId();
        Order order = orderRepository.findById(userId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + userId));
        return orderMapper.convertOrderToDTO(order);
    }

    public Order getOrderOrCreateIfNotExistByOwner() {
        User owner = userService.getCurrentUser();
        Optional<Order> optionalOrder = orderRepository
                .findOrderByOrderStatusUnpaidAndOwner(owner);
        return optionalOrder.orElseGet(() -> orderRepository.save(Order.builder()
                .dateCreated(LocalDateTime.now())
                .orderStatus(OrderStatus.UNPAID)
                .owner(owner)
                .build()));
    }

    @Transactional
    public MessageResponse paymentForOrder() {
        Order order = orderRepository
                .findOrderByOrderStatusUnpaidAndOwner(userService.getCurrentUser())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        orderRepository.deleteAllProductsFromOrder(order.getId());
        orderRepository.delete(order);
        return new MessageResponse("Payment was successful");
    }

    @Scheduled(fixedRate = 30_000)
    @Transactional
    public void deleteUnpaidOrders() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        List<Order> unpaidOrders = orderRepository
                .findByOrderStatusAndDateCreatedBefore(OrderStatus.UNPAID, tenMinutesAgo);

        for (Order order :
                unpaidOrders) {
            orderRepository.updateProductStatusesToFreeInOrder(order.getId());
        }

        orderRepository.deleteAll(unpaidOrders);
    }
}
