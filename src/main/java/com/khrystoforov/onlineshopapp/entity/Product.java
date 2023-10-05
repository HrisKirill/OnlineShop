package com.khrystoforov.onlineshopapp.entity;

import com.khrystoforov.onlineshopapp.entity.enums.ProductStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "products")
@Getter
@EqualsAndHashCode(exclude = {"id", "status"})
@ToString(exclude = {"id"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "name_index", columnList = "name")
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 32)
    private String name;
    private Double price;
    @Enumerated(EnumType.STRING)
    @Setter
    private ProductStatus status = ProductStatus.FREE;

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}
