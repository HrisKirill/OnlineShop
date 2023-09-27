package com.khrystoforov.onlineshopapp.repository;

import com.khrystoforov.onlineshopapp.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
