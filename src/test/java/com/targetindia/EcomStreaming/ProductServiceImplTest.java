package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.controllers.CustomerController;
import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.exceptions.ProductNotFoundException;
import com.targetindia.EcomStreaming.exceptions.ProductQuantityException;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import com.targetindia.EcomStreaming.repository.ProductRepository;
import com.targetindia.EcomStreaming.service.ArchivedOrdersServiceImpl;
import com.targetindia.EcomStreaming.service.KafkaConsumer;
import com.targetindia.EcomStreaming.service.OrderServiceImpl;
import com.targetindia.EcomStreaming.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceImplTest {

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private ArchivedOrdersServiceImpl archivedOrdersService;
    @MockBean
    private KafkaConsumer kafkaConsumer;
    @MockBean
    private CustomerController customerController;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private OrderServiceImpl orderService;

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
    public void testFetchProductStockLevel_ValidId() throws ProductNotFoundException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Long stockLevel = productService.fetchProductStockLevel(1L);
        assertEquals(10L, stockLevel);
    }

    @Test
    public void testFetchProductStockLevel_InvalidId() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.fetchProductStockLevel(2L);
        });
        assertEquals("Invalid Product ID:2", exception.getMessage());
    }

    @Test
    public void testSetProductStockLevel_Valid() throws ProductNotFoundException, ProductNotFoundException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.setProductStockLevel(1L, 5L);
        verify(productRepository, times(1)).findById(1L);
        assertEquals(5L, product.getStockLevel());
    }

    @Test
    public void testSetProductStockLevel_InvalidStockLevel(){
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            productService.setProductStockLevel(1L, 0L);
        });
        assertEquals("No value present", exception.getMessage());
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
