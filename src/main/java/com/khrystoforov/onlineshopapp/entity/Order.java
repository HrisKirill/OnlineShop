package com.khrystoforov.onlineshopapp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import lombok.*;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity(name = "orders")
@Getter
@EqualsAndHashCode(exclude = {"id", "owner", "products"})
@ToString(exclude = {"id"})
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.UNPAID;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;


    @ElementCollection
    @Column(name = "quantity", nullable = false)
    @MapKeyJoinColumn(name = "product_id")
    private Map<Product, Integer> products = new HashMap<>();

    public Order(User owner) {
        this.owner = owner;
    }

    public Map<Product, Integer> getProducts() {
        return Collections.unmodifiableMap(products);
    }

    public void addProduct(Product item) {
        products.merge(item, 1, Integer::sum);
    }

    public void removeProduct(Product item) {
        products.computeIfPresent(item, (k, v) -> v > 1 ? v - 1 : null);
    }

    @Transient
    public Double getTotalOrderPrice() {
        double sum = 0D;
        Map<Product, Integer> products = getProducts();
        for (Map.Entry<Product, Integer> entry :
                products.entrySet()) {
            sum += entry.getKey().getPrice() * entry.getValue();
        }
        return sum;
    }
}
