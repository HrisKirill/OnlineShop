package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.OrderProduct;
import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.exception.ProductNotFoundException;
import com.khrystoforov.onlineshopapp.exception.ProductQuantityException;
import com.khrystoforov.onlineshopapp.repository.OrderProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final OrderService orderService;
    private final ProductService productService;

    public OrderProduct addProductsToOrder(Long orderId, String productName, Integer quantity) {
        Order order = orderService.getOrderById(orderId);
        List<Product> allProductsWithThisName = productService.findAllProductsByName(productName);

        if (allProductsWithThisName.isEmpty()) {
            throw new ProductNotFoundException(productName + " is not available");
        } else if (allProductsWithThisName.size() < quantity) {
            throw new ProductQuantityException("Please, make an order of the " + productName
                    + " less than or equal to the quantity " + allProductsWithThisName.size() + ".");
        }

        return orderProductRepository.
                save(new OrderProduct(order, allProductsWithThisName.get(0), quantity));
    }

}
