package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.controllers.OrderController;
import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.exceptions.CustomerIdException;
import com.targetindia.EcomStreaming.exceptions.ProductQuantityException;
import com.targetindia.EcomStreaming.model.Product;
import com.targetindia.EcomStreaming.service.CustomerService;
import com.targetindia.EcomStreaming.service.KafkaProducer;
import com.targetindia.EcomStreaming.service.OrderService;
import com.targetindia.EcomStreaming.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublish() throws ProductQuantityException {
        // Arrange
        Product product = new Product();
        product.setProductID(1L);
        product.setProductQuantity(5L);

        Order order = new Order();
        order.setOrderID(1L);
        order.setProductList(Collections.singletonList(product));

        when(productService.fetchProductStockLevel(any(Long.class)))
                .thenReturn(CompletableFuture.completedFuture(10L));

        // Act
        orderController.publish(order);

        // Assert
        verify(productService, times(1)).setProductStockLevel(1L, 5L);
        verify(kafkaProducer, times(1)).sendMessage(order);
    }

    @Test
    void testPublishProductQuantityException() {
        // Arrange
        Product product = new Product();
        product.setProductID(1L);
        product.setProductQuantity(15L);

        Order order = new Order();
        order.setOrderID(1L);
        order.setProductList(Collections.singletonList(product));

        when(productService.fetchProductStockLevel(any(Long.class)))
                .thenReturn(CompletableFuture.completedFuture(10L));

        // Act & Assert
        CompletionException exception = assertThrows(CompletionException.class, () -> orderController.publish(order));
        assertEquals(ProductQuantityException.class, exception.getCause().getClass());
        assertEquals("Product ID: 1's quantity is more than stock level. Stock level left: 10", exception.getCause().getMessage());
    }


    @Test
    void testFetchOrderList() {
        // Arrange
        Order order1 = new Order();
        order1.setOrderID(1L);
        Order order2 = new Order();
        order2.setOrderID(2L);

        when(orderService.fetchOrderList()).thenReturn(Arrays.asList(order1, order2));

        // Act
        List<Order> result = orderController.fetchOrderList();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getOrderID());
        assertEquals(2L, result.get(1).getOrderID());
    }

    @Test
    void testFetchOrderListByCustomerID() {
        // Arrange
        Customer customer = new Customer();
        customer.setCustomerID(1L);

        Order order1 = new Order();
        order1.setOrderID(1L);
        Order order2 = new Order();
        order2.setOrderID(2L);

        when(customerService.getCustomerByID(any(Long.class))).thenReturn(Optional.of(customer));
        when(orderService.fetchOrderListByID(any(Long.class))).thenReturn(Arrays.asList(order1, order2));

        // Act
        List<Order> result = orderController.fetchOrderListByCustomerID(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getOrderID());
        assertEquals(2L, result.get(1).getOrderID());
    }

    @Test
    void testFetchOrderListByCustomerIDCustomerIdException() {
        // Arrange
        when(customerService.getCustomerByID(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerIdException.class, () -> orderController.fetchOrderListByCustomerID(1L));
    }
}
