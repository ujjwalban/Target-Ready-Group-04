package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.ArchivedOrders;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.repository.ArchivedOrdersRepository;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArchivedOrdersServiceImpl implements ArchivedOrdersService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ArchivedOrdersRepository archivedOrderRepository;

    private static final Logger logger = LoggerFactory.getLogger(ArchivedOrdersServiceImpl.class);

    @Override
    public void archiveExpiredOrders() {
        Date currentDate = new Date();
        List<Order> expiredOrders = orderRepository.findByExpiryDateBefore(currentDate);
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

