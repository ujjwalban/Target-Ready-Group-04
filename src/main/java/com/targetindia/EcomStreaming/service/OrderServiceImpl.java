package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
//import java.util.Objects;
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> fetchOrderList() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> fetchOrderListByID(Long orderID){
        return orderRepository.getHistory(orderID);
    }

    @Override
    public List<Order> expiredOrders(){
        Date currentDate = new Date();
        return orderRepository.findByExpiryDateBefore(currentDate);
    }

}
