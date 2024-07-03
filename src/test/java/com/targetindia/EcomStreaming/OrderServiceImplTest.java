package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.exceptions.OrderIdException;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import com.targetindia.EcomStreaming.service.OrderServiceImpl;
import com.targetindia.EcomStreaming.service.ArchivedOrdersServiceImpl;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @MockBean
    private OrderRepository orderRepository;

    private Order order;

    @MockBean
    private CircuitBreakerRegistry circuitBreakerRegistry;

    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        circuitBreaker = CircuitBreaker.ofDefaults("orderService");
        when(circuitBreakerRegistry.circuitBreaker("orderService")).thenReturn(circuitBreaker);
    }

    @Test
    public void testFetchOrderList() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findAll()).thenReturn(orders);
        List<Order> result = orderService.fetchOrderList();
        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
    }

    @Test
    public void testFetchOrderListByUsernameFallback() {
        // Simulate service failure
        when(orderRepository.getAllOrderByUsername("testUsername")).thenThrow(new RuntimeException("Simulated service failure"));

        List<Order> result = orderService.fetchOrderListByUsername("testUsername");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFetchOrderListByUsernameCircuitBreakerOpen() {
        // Simulate circuit breaker open state
        circuitBreaker.transitionToOpenState();

        List<Order> result = orderService.fetchOrderListByUsername("testUsername");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFetchOrderListByID_ValidId() throws OrderIdException {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.getHistory(1L)).thenReturn(orders);
        List<Order> result = orderService.fetchOrderListByID(1L);
        assertEquals(1, result.size());
        assertEquals(order, result.get(0));
    }

    @Test
    public void testFetchOrderListByID_InvalidId() {
        when(orderRepository.getHistory(2L)).thenThrow(new RuntimeException());
        OrderIdException exception = assertThrows(OrderIdException.class, () -> {
            orderService.fetchOrderListByID(2L);
        });
        assertEquals("Invalid Order ID: 2", exception.getMessage());
    }

}
