package com.khrystoforov.onlineshopapp.repository;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderStatusAndDateCreatedBefore(OrderStatus orderStatus, LocalDateTime timeCreated);

    @Query("SELECT o from orders  o where  o.owner=?1 and o.orderStatus=1")
    Optional<Order> findOrderByOrderStatusUnpaidAndOwner(User user);
}
