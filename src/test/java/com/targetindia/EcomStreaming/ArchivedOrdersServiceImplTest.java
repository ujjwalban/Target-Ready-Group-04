package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.entites.ArchivedOrders;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.repository.ArchivedOrdersRepository;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import com.targetindia.EcomStreaming.service.ArchivedOrdersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ArchivedOrdersServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ArchivedOrdersRepository archivedOrderRepository;

    @InjectMocks
    private ArchivedOrdersServiceImpl archivedOrdersServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testArchiveExpiredOrders() {
        // Arrange
        Order order1 = new Order();
        order1.setOrderID(1L);
        order1.setCustomerID(101L);
        order1.setDate(new Date(System.currentTimeMillis() - 100000));
        order1.setProductList(Collections.emptyList());
        order1.setExpiryDate(new Date(System.currentTimeMillis() - 200000));

        Order order2 = new Order();
        order2.setOrderID(2L);
        order2.setCustomerID(102L);
        order2.setDate(new Date(System.currentTimeMillis() - 100000));
        order2.setProductList(Collections.emptyList());
        order2.setExpiryDate(new Date(System.currentTimeMillis() - 200000));

        when(orderRepository.findByExpiryDateBefore(any(Date.class))).thenReturn(Arrays.asList(order1, order2));

        // Act
        archivedOrdersServiceImpl.archiveExpiredOrders();

        // Assert
        ArgumentCaptor<List<ArchivedOrders>> captor = ArgumentCaptor.forClass(List.class);
        verify(archivedOrderRepository, times(1)).saveAll(captor.capture());
        List<ArchivedOrders> archivedOrdersList = captor.getValue();

        assertEquals(2, archivedOrdersList.size());
        assertEquals(1L, archivedOrdersList.get(0).getOrderID());
        assertEquals(101L, archivedOrdersList.get(0).getCustomerID());
        assertEquals(2L, archivedOrdersList.get(1).getOrderID());
        assertEquals(102L, archivedOrdersList.get(1).getCustomerID());

        verify(orderRepository, times(1)).deleteAll(Arrays.asList(order1, order2));
    }

    @Test
    void testArchiveExpiredOrdersNoExpiredOrders() {
        // Arrange
        when(orderRepository.findByExpiryDateBefore(any(Date.class))).thenReturn(Collections.emptyList());

        // Act
        archivedOrdersServiceImpl.archiveExpiredOrders();

        // Assert
        verify(archivedOrderRepository, never()).saveAll(anyList());
        verify(orderRepository, never()).deleteAll(anyList());
    }
}
