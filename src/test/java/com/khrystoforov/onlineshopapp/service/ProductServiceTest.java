package com.khrystoforov.onlineshopapp.service;

import com.khrystoforov.onlineshopapp.entity.Product;
import com.khrystoforov.onlineshopapp.entity.enums.ProductStatus;
import com.khrystoforov.onlineshopapp.mapper.ProductMapper;
import com.khrystoforov.onlineshopapp.payload.dto.ProductDTO;
import com.khrystoforov.onlineshopapp.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product getTestProduct() {
        return Product.builder()
                .id(1L)
                .name("test")
                .price(500.)
                .status(ProductStatus.FREE)
                .build();
    }

    @Test
    void testCreate() {
        when(productRepository.save(getTestProduct()))
                .thenReturn(getTestProduct());
        assertEquals(getTestProduct(), productService.create(getTestProduct()));
    }

    @Test
    void testFindFreeProducts() {
        List<Product> list = List.of(getTestProduct(), Product.builder()
                .name("test2")
                .price(500.)
                .id(2L)
                .status(ProductStatus.IN_PROCESSING)
                .build());

        when(productRepository.findAll()).thenReturn(list);

        ProductDTO freeProduct = productMapper.productToProductDTO(getTestProduct(), 1);

        List<ProductDTO> productDTOS = new ArrayList<>();
        productDTOS.add(freeProduct);

        assertEquals(productDTOS, productService.findFreeProducts());
    }

    @Test
    void testAddProducts() {
        Product product = Product.builder()
                .name(getTestProduct().getName())
                .price(getTestProduct().getPrice())
                .status(getTestProduct().getStatus())
                .build();

        when(productRepository.saveAll(List.of(product)))
                .thenReturn(List.of(product));
        ProductDTO productDTO = productMapper.productToProductDTO(product, 1);

        assertEquals(productDTO, productService.addProducts(product, 1));
    }

    @Test
    void testFindAllProductsByNameAndStatus() {
        getTestProduct().setStatus(ProductStatus.FREE);
        when(productRepository.findAllByNameAndStatus(getTestProduct().getName(),
                ProductStatus.FREE)).thenReturn(List.of(getTestProduct()));

        assertEquals(List.of(getTestProduct()),
                productService.findAllProductsByNameAndStatus(getTestProduct().getName(), ProductStatus.FREE));
    }

    @Test
    public void testUpdateProductStatus() {

        doNothing().when(productRepository)
                .updateProductStatusById(getTestProduct().getStatus(), getTestProduct().getId());

        productService.updateProductStatus(getTestProduct().getStatus(), getTestProduct().getId());

        verify(productRepository, times(1))
                .updateProductStatusById(getTestProduct().getStatus(), getTestProduct().getId());
    }
}