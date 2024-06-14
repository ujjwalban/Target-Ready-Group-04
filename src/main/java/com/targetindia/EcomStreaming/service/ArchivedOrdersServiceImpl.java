package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.ArchivedOrders;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.repository.ArchivedOrdersRepository;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArchivedOrdersServiceImpl implements ArchivedOrdersService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ArchivedOrdersRepository archivedOrderRepository;

    private static final Logger logger = LoggerFactory.getLogger(ArchivedOrdersServiceImpl.class);

    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(order -> order.setExpiryDate(calculateExpiryDate(order)));
        return orders;
    }

    public List<Order> getOrdersExpiringBefore(Date dateTime) {
        List<Order> orders = getAllOrders();
        return orders.stream()
                .filter(order -> order.getExpiryDate().after(new Date(30)))
                .collect(Collectors.toList());
    }

    private LocalDateTime calculateExpiryDate(Order order) {
        // Define your logic to calculate the expiry date for each order
        return LocalDateTime.now().plusDays(30); // For example, set expiry date to 30 days from now
    }
    @Override
    public void archiveExpiredOrders() {
        Date currentDate = new Date();
        List<Order> expiredOrders = getOrdersExpiringBefore(currentDate);
        if (!expiredOrders.isEmpty()) {
            List<ArchivedOrders> archivedOrders = new ArrayList<>();
            for (Order order : expiredOrders) {
                ArchivedOrders archivedOrder = new ArchivedOrders();
                archivedOrder.setOrderID(order.getOrderID());
                archivedOrder.setCustomerID(order.getCustomerID());
                archivedOrder.setDate(order.getDate());
                archivedOrder.setProductList(new ArrayList<>(order.getProductList()));

                logger.debug("Archiving order: {}", archivedOrder);
                logger.debug("Product list: {}", archivedOrder.getProductList());

                archivedOrders.add(archivedOrder);
            }
            archivedOrderRepository.saveAll(archivedOrders);
            orderRepository.deleteAll(expiredOrders);
        }
    }
}

