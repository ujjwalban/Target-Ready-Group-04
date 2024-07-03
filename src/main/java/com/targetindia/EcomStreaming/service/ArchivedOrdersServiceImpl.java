package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.ArchivedOrders;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.repository.ArchivedOrdersRepository;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArchivedOrdersServiceImpl implements ArchivedOrdersService {

    @Autowired
    private static OrderRepository orderRepository;

    @Autowired
    private ArchivedOrdersRepository archivedOrderRepository;

    private static final Logger logger = LoggerFactory.getLogger(ArchivedOrdersServiceImpl.class);

    public static List<Order> getOrdersExpiringBefore(Date currentDate) {
        List<Order> filteredOrders = new ArrayList<>();
        List<Order> history = orderRepository.findAll();
        // Subtract 30 days from the current date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date date30DaysAgo = calendar.getTime();
        for(Order o:history){
            Date orderDate = o.getDate();
            if(orderDate.before(date30DaysAgo)){
                filteredOrders.add(o);
            }
        }
        return filteredOrders;
    }

    public static List<Order> getAllOrders() {
        return new ArrayList<>();
    }


    @Override
    @CircuitBreaker(name = "archivedOrdersService", fallbackMethod = "fallbackArchiveExpiredOrders")
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
    public void fallbackArchiveExpiredOrders(Throwable throwable) {
        // Log the exception or handle it as needed
        logger.error("Fallback method triggered due to: {}", throwable.getMessage());

        // Implement fallback logic, such as notifying administrators or retrying later
    }
}

