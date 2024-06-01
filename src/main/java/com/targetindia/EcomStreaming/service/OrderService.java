package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Order;

import java.util.List;

public interface OrderService {

    List<Order> fetchOrderList();
    Order fetchOrderListByID(Long orderID);
}
