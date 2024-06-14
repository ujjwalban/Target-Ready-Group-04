package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.exceptions.OrderIdException;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

}
