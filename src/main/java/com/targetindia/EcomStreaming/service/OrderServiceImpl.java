package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.exceptions.OrderIdException;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> fetchOrderList() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> fetchOrderListByID(Long orderID) throws OrderIdException {
        try {
            return orderRepository.getHistory(orderID);
        } catch (Exception e) {
            throw new OrderIdException("Invalid Order ID: " + orderID);
        }
    }

    @Override
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackFetchOrderListByUsername")
    public List<Order> fetchOrderListByUsername(String customerUsername) {
        return orderRepository.getAllOrderByUsername(customerUsername);
    }

    public List<Order> fallbackFetchOrderListByUsername(String customerUsername, Throwable throwable) {
        // Log the exception or handle it as needed
        System.out.println("Fallback method triggered due to: " + throwable.getMessage());

        // Return an empty list or a default value
        return Collections.emptyList();
    }

}
