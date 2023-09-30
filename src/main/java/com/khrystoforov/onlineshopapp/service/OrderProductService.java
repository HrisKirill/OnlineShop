package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.OrderProduct;
import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.entity.enums.ProductStatus;
import com.khrystoforov.onlineshopapp.exception.ProductNotFoundException;
import com.khrystoforov.onlineshopapp.exception.ProductQuantityException;
import com.khrystoforov.onlineshopapp.payload.response.MessageResponse;
import com.khrystoforov.onlineshopapp.repository.OrderProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final OrderService orderService;
    private final ProductService productService;

    @Transactional
    public MessageResponse addProductsToOrder(String productName, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity mast be above zero");
        }

        Order order = orderService.getOrderOrCreateIfNotExistByOwner();
        List<Product> allProductsWithThisName = productService
                .findAllProductsByNameAndStatus(productName, ProductStatus.FREE);

        if (allProductsWithThisName.isEmpty()) {
            throw new ProductNotFoundException(productName + " is not available");
        } else if (allProductsWithThisName.size() < quantity) {
            throw new ProductQuantityException("Please, make an order of the " + productName
                    + " less than or equal to the quantity " + allProductsWithThisName.size() + ".");
        }

        for (int i = 0; i < quantity; i++) {
            productService.updateProductStatus(ProductStatus.IN_PROCESSING,
                    allProductsWithThisName.get(i).getId());

            orderProductRepository.
                    save(new OrderProduct(order, allProductsWithThisName.get(i)));
        }

        return new MessageResponse("Products add to order successfully");
    }

}
