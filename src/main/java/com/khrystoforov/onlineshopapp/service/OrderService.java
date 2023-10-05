package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import com.khrystoforov.onlineshopapp.entity.enums.ProductStatus;
import com.khrystoforov.onlineshopapp.exception.OrderNotFoundException;
import com.khrystoforov.onlineshopapp.exception.ProductNotFoundException;
import com.khrystoforov.onlineshopapp.exception.ProductQuantityException;
import com.khrystoforov.onlineshopapp.mapper.OrderMapper;
import com.khrystoforov.onlineshopapp.payload.dto.OrderDTO;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;
    private final ProductService productService;

    public OrderDTO getUserOrder() {
        Long userId = userService.getCurrentUser().getId();
        Order order = orderRepository.findOrderByOwnerId(userId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with owner id " + userId));
        return orderMapper.orderToOrderDTO(order, userService.getCurrentUser());
    }

    public Order getOrderOrCreateIfNotExistByOwner() {
        User owner = userService.getCurrentUser();
        Optional<Order> optionalOrder = orderRepository
                .findOrderByOwner(owner);
        return optionalOrder.orElseGet(() -> orderRepository.save(new Order(owner)));
    }

    @Transactional
    public MessageResponse paymentForOrder() {
        Order order = orderRepository
                .findOrderByOwner(userService.getCurrentUser())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        for (Map.Entry<Product, Integer> entry :
                order.getProducts().entrySet()) {
            productService.deleteInProcessingProductByName(entry.getKey().getName(),
                    entry.getValue());
        }
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

    @Transactional
    public MessageResponse addProductsToOrder(String productName, Integer quantity) {
        Order order = getOrderOrCreateIfNotExistByOwner();
        List<Product> allProductsWithThisName = productService.findAllProductsByNameAndStatus(productName, ProductStatus.FREE);

        if (allProductsWithThisName.isEmpty()) {
            throw new ProductNotFoundException(productName + " is not available");
        } else if (allProductsWithThisName.size() < quantity) {
            throw new ProductQuantityException("Please, make an order of the " + productName
                    + " less than or equal to the quantity " + allProductsWithThisName.size() + ".");
        }

        for (int i = 0; i < quantity; i++) {
            Product productToAdd = allProductsWithThisName.get(i);
            productToAdd.setStatus(ProductStatus.IN_PROCESSING);
            order.addProduct(productToAdd);
        }

        order.setOwner(userService.getCurrentUser());
        orderRepository.save(order);
        return new MessageResponse("Products add to order successfully");
    }

}
