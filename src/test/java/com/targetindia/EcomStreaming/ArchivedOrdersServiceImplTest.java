package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.repository.ArchivedOrdersRepository;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import com.targetindia.EcomStreaming.service.ArchivedOrdersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ArchivedOrdersServiceImplTest {

    @Autowired
    private ArchivedOrdersServiceImpl archivedOrdersService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ArchivedOrdersRepository archivedOrderRepository;

    private Order expiredOrder;

    @BeforeEach
    public void setUp() {
        expiredOrder = new Order();
        expiredOrder.setOrderID(1L);
        expiredOrder.setCustomerID(1L);
        expiredOrder.setDate(new Date(System.currentTimeMillis() - 86400000)); // testing expiry period: -1 day
        expiredOrder.setProductList(new ArrayList<>());
    }

    @Test
    public void testArchiveExpiredOrders_WithExpiredOrders() {
        List<Order> expiredOrders = new ArrayList<>();
        expiredOrders.add(expiredOrder);

        when(orderRepository.findByExpiryDateBefore(any(Date.class))).thenReturn(expiredOrders);

        archivedOrdersService.archiveExpiredOrders();

        verify(archivedOrderRepository, times(1)).saveAll(anyList());
        verify(orderRepository, times(1)).deleteAll(expiredOrders);
    }

    @Test
    public void testArchiveExpiredOrders_NoExpiredOrders() {
        List<Order> expiredOrders = new ArrayList<>();

        when(orderRepository.findByExpiryDateBefore(any(Date.class))).thenReturn(expiredOrders);

        archivedOrdersService.archiveExpiredOrders();

        verify(archivedOrderRepository, times(0)).saveAll(anyList());
        verify(orderRepository, times(0)).deleteAll(anyList());
    }
}
