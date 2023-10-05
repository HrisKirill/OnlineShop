package com.khrystoforov.onlineshopapp.repository;

import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.entity.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByNameAndStatus(String name, ProductStatus status);

    @Modifying
    @Query("update products p set p.status= ?1 where p.id = ?2")
    void updateProductStatusById(ProductStatus productStatus, Long id);

    @Modifying
    @Query(value = "delete from products  where name=?1 and status='IN_PROCESSING' " +
            " order by id limit ?2", nativeQuery = true)
    void deleteInProcessingProductByName(String name, Integer limit);
}
