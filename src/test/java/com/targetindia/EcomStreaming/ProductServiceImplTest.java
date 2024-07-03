package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.exceptions.ProductNotFoundException;
import com.targetindia.EcomStreaming.repository.ProductRepository;
import com.targetindia.EcomStreaming.service.ProductServiceImpl;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CircuitBreaker circuitBreaker;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductByID_Success() {
        // Mock data
        Long productId = 1L;
        Products product = new Products();
        product.setProductID(productId);
        product.setProductName("Test Product");

        // Mock repository behavior
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Call service method
        Optional<Products> result = productService.getProductByID(productId);

        // Verify
        assertEquals(product.getProductName(), result.orElseThrow().getProductName());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductByID_ProductNotFoundException() {
        // Mock data
        Long productId = 1L;

        // Mock repository behavior
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Call service method and expect exception
        ProductNotFoundException thrown = assertThrows(ProductNotFoundException.class,
                () -> productService.getProductByID(productId));

        // Verify
        assertNotNull(thrown);
        verify(productRepository, times(1)).findById(productId);
    }


    @Test
    void testFetchProductStockLevel_Success() {
        // Mock data
        Long productId = 1L;
        Long stockLevel = 10L;
        Products product = new Products();
        product.setProductID(productId);
        product.setStockLevel(stockLevel);

        // Mock repository behavior
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Call service method
        Long result = productService.fetchProductStockLevel(productId);

        // Verify
        assertEquals(stockLevel, result);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testFetchProductStockLevel_ProductNotFoundException() {
        // Mock data
        Long productId = 1L;

        // Mock repository behavior
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Call service method and expect exception
        ProductNotFoundException thrown = assertThrows(ProductNotFoundException.class,
                () -> productService.fetchProductStockLevel(productId));

        // Verify
        assertNotNull(thrown);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testSetProductStockLevel_Success() {
        // Mock data
        Long productId = 1L;
        Long newStockLevel = 20L;
        Products product = new Products();
        product.setProductID(productId);

        // Mock repository behavior
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Call service method
        productService.setProductStockLevel(productId, newStockLevel);

        // Verify
        assertEquals(newStockLevel, product.getStockLevel());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testDisplayAllProducts_Success() {
        // Mock data
        List<Products> productList = new ArrayList<>();
        productList.add(new Products(1L, "Product1", 20L, 45.0));
        productList.add(new Products(2L, "Product2", 30L, 56.0));

        // Mock repository behavior
        when(productRepository.findAll()).thenReturn(productList);

        // Call service method
        List<Products> result = productService.displayAllProducts();

        // Verify
        assertEquals(productList.size(), result.size());
        assertEquals(productList.get(0).getProductName(), result.get(0).getProductName());
        assertEquals(productList.get(1).getProductName(), result.get(1).getProductName());
        verify(productRepository, times(1)).findAll();
    }
}
