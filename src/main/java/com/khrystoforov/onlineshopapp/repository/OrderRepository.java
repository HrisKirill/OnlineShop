package com.khrystoforov.onlineshopapp.repository;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderByOwnerId(Long ownerId);

    List<Order> findByOrderStatusAndDateCreatedBefore(OrderStatus orderStatus, LocalDateTime timeCreated);

    @Query("SELECT o from orders  o where  o.owner=?1 and o.orderStatus=1")
    Optional<Order> findOrderByOrderStatusUnpaidAndOwner(User user);

    @Modifying
    @Query(value = "update products set products.status=1 where id in ( select id from " +
            "(select id from products join order_product  " +
            "on products.id = order_product.product_id and order_product.order_id=?1 )tmp)", nativeQuery = true)
    void updateProductStatusesToFreeInOrder(Long orderId);

    @Modifying
    @Query(value = "delete from products where id in (select id from" +
            "(select id from products join order_product " +
            " on products.id = order_product.product_id and order_product.order_id=?1 )tmp)", nativeQuery = true)
    void deleteAllProductsFromOrder(Long orderId);
}
