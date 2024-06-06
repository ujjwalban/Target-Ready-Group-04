package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.exceptions.ProductIdException;
import com.targetindia.EcomStreaming.exceptions.StockLevelException;
import com.targetindia.EcomStreaming.repository.ProductRepository;
import com.targetindia.EcomStreaming.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @MockBean
    private ProductRepository productRepository;

    private Products product;

    @BeforeEach
    public void setUp() {
        product = new Products();
        product.setProductID(1L);
        product.setStockLevel(10L);
    }

    @Test
    public void testFetchProductStockLevel_ValidId() throws ProductIdException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Long stockLevel = productService.fetchProductStockLevel(1L);
        assertEquals(10L, stockLevel);
    }

    @Test
    public void testFetchProductStockLevel_InvalidId() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        ProductIdException exception = assertThrows(ProductIdException.class, () -> {
            productService.fetchProductStockLevel(2L);
        });
        assertEquals("Invalid Product ID:2", exception.getMessage());
    }

    @Test
    public void testSetProductStockLevel_Valid() throws StockLevelException, ProductIdException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.setProductStockLevel(1L, 5L);
        verify(productRepository, times(1)).findById(1L);
        assertEquals(5L, product.getStockLevel());
    }

    @Test
    public void testSetProductStockLevel_InvalidStockLevel() {
        StockLevelException exception = assertThrows(StockLevelException.class, () -> {
            productService.setProductStockLevel(1L, 0L);
        });
        assertEquals("Invalid Stock Level: 0", exception.getMessage());
    }

    @Test
    public void testDisplayAllProducts() {
        List<Products> productsList = new ArrayList<>();
        productsList.add(product);
        when(productRepository.findAll()).thenReturn(productsList);
        List<Products> result = productService.displayAllProducts();
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }
}
