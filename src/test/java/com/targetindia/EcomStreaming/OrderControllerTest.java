package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.controllers.OrderController;
import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.exceptions.ProductNotFoundException;
import com.targetindia.EcomStreaming.exceptions.ProductQuantityException;
import com.targetindia.EcomStreaming.model.Product;
import com.targetindia.EcomStreaming.service.CustomerService;
import com.targetindia.EcomStreaming.service.KafkaProducer;
import com.targetindia.EcomStreaming.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private ProductService productService;

    @Mock
    private CustomerService customerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublishOrderSuccess() throws Exception {
        // Arrange
        Order order = new Order();
        order.setCustomerUsername("test_user");
        Product product = new Product();
        product.setProductID(1L);
        product.setProductQuantity(5L);
        order.setProductList(List.of(product));

        Customer customer = new Customer();
        customer.setCustomerID(1L);
        when(customerService.getCustomerByUsername("test_user")).thenReturn(Optional.of(customer));
        when(customerService.findByUsername("test_user")).thenReturn(customer);
        when(productService.fetchProductStockLevel(1L)).thenReturn(10L);

        // Act
        CompletableFuture<ResponseEntity<String>> responseFuture = orderController.publish(order);

        // Assert
        assertNotNull(responseFuture);
        ResponseEntity<String> response = responseFuture.get();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Order is placed", response.getBody());
    }

    @Test
    void testPublishOrderProductQuantityException() throws Exception {
        // Arrange
        Order order = new Order();
        order.setCustomerUsername("test_user");
        Product product = new Product();
        product.setProductID(1L);
        product.setProductQuantity(15L);
        order.setProductList(List.of(product));

        Customer customer = new Customer();
        customer.setCustomerID(1L);
        when(customerService.getCustomerByUsername("test_user")).thenReturn(Optional.of(customer));
        when(customerService.findByUsername("test_user")).thenReturn(customer);
        when(productService.fetchProductStockLevel(1L)).thenReturn(10L);

        // Act & Assert
        CompletableFuture<ResponseEntity<String>> responseFuture = orderController.publish(order);

        assertThrows(Exception.class, responseFuture::get);
    }

    @Test
    void testPublishOrderProductNotFoundException() throws Exception {
        // Arrange
        Order order = new Order();
        order.setCustomerUsername("test_user");
        Product product = new Product();
        product.setProductID(1L);
        product.setProductQuantity(5L);
        order.setProductList(List.of(product));

        when(customerService.getCustomerByUsername("test_user")).thenReturn(Optional.empty());

        // Act & Assert
        CompletableFuture<ResponseEntity<String>> responseFuture = orderController.publish(order);

        assertThrows(Exception.class, responseFuture::get);
    }
}
