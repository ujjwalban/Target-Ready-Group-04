package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Order;

import java.util.List;

public interface OrderService {

    List<Order> fetchOrderList();
    List<Order> fetchOrderListByID(Long id);
    List<Order> expiredOrders();
}
