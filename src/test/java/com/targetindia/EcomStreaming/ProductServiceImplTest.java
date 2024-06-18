package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.exceptions.ProductIdException;
import com.targetindia.EcomStreaming.exceptions.StockLevelException;
import com.targetindia.EcomStreaming.repository.ProductRepository;
import com.targetindia.EcomStreaming.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Executor executor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        executor = Executors.newFixedThreadPool(10); // Custom thread pool for testing
    }

    @Test
    void testFetchProductStockLevelSuccess() throws ExecutionException, InterruptedException {
        Products product = new Products();
        product.setStockLevel(100L);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        CompletableFuture<Long> future = productService.fetchProductStockLevel(1L);

        assertEquals(100L, future.get());
    }

    @Test
    void testFetchProductStockLevelProductIdException() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        CompletableFuture<Long> future = productService.fetchProductStockLevel(1L);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertEquals(ProductIdException.class, exception.getCause().getClass());
        assertEquals("Product with ID 1 not found", exception.getCause().getMessage());
    }

    @Test
    void testSetProductStockLevelSuccess() throws StockLevelException {
        Products product = new Products();
        product.setStockLevel(50L);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        productService.setProductStockLevel(1L, 100L);

        assertEquals(100L, product.getStockLevel());
    }

    @Test
    void testSetProductStockLevelInvalidStockLevel() {
        Products product = new Products();
        product.setStockLevel(50L);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        StockLevelException exception = assertThrows(StockLevelException.class, () -> {
            productService.setProductStockLevel(1L, -10L);
        });

        assertEquals("Invalid Stock Level: -10", exception.getMessage());
    }

    @Test
    void testDisplayAllProducts() {
        List<Products> productsList = List.of(new Products(), new Products());
        when(productRepository.findAll()).thenReturn(productsList);

        List<Products> result = productService.displayAllProducts();

        assertEquals(2, result.size());
    }

    @Test
    void testFetchProductStockLevelAsync() throws ExecutionException, InterruptedException {
        Products product = new Products();
        product.setStockLevel(100L);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        CompletableFuture<Long> future1 = productService.fetchProductStockLevel(1L);
        CompletableFuture<Long> future2 = productService.fetchProductStockLevel(2L);
        CompletableFuture<Long> future3 = productService.fetchProductStockLevel(3L);

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);

        allFutures.get();

        assertTrue(future1.isDone());
        assertTrue(future2.isDone());
        assertTrue(future3.isDone());

        assertEquals(100L, future1.get());
        assertEquals(100L, future2.get());
        assertEquals(100L, future3.get());
    }
}
