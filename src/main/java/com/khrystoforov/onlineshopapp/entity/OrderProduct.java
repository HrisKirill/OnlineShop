package com.khrystoforov.onlineshopapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProduct {

    @EmbeddedId
    @JsonIgnore
    private OrderProductPK pk;

    public OrderProduct(Order order, Product product) {
        pk = new OrderProductPK();
        pk.setOrder(order);
        pk.setProduct(product);
    }

    @Transient
    public Product getProduct() {
        return this.pk.getProduct();
    }


}
