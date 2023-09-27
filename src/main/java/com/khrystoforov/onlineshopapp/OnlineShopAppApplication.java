package com.khrystoforov.onlineshopapp;

import com.khrystoforov.onlineshopapp.entity.Order;
import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.entity.enums.OrderStatus;
import com.khrystoforov.onlineshopapp.service.OrderService;
import com.khrystoforov.onlineshopapp.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootApplication
@EnableScheduling
public class OnlineShopAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShopAppApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(ProductService productService,
                             OrderService orderService) {
        return args -> {
            productService.create(new Product(1L, "IPhone 13 Pro", 1000.));
            productService.create(new Product(2L, "IPhone 13 Pro", 1000.));
            productService.create(new Product(3L, "IPhone 13 Pro", 1000.));
            productService.create(new Product(4L, "IPhone 13 Pro", 1000.));

            productService.create(new Product(5L, "IPhone 11", 850.));
            productService.create(new Product(6L, "IPhone 11", 850.));
            productService.create(new Product(7L, "IPhone 11", 850.));

            productService.create(new Product(8L, "IPhone 7 Plus", 500.));
            productService.create(new Product(9L, "IPhone 7 Plus", 500.));

            orderService.create(new Order(1L, LocalDateTime.now(), OrderStatus.UNPAID, new ArrayList<>()));

        };
    }
}
