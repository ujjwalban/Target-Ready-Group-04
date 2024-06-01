package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//import java.util.Objects;
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> fetchOrderList() {
        return (List<Order>)
                orderRepository.findAll();
    }

    @Override
    public Order fetchOrderListByID(Long orderID){
        return orderRepository.findById(orderID).get();
    }
}
