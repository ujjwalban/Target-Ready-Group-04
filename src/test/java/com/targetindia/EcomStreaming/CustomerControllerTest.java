package com.targetindia.EcomStreaming;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.targetindia.EcomStreaming.controllers.CustomerController;
import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.service.CustomerService;
import com.targetindia.EcomStreaming.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testFetchOrderListByCustomerID() throws Exception {
        Long customerID = 1L;

        Customer customer = new Customer();
        customer.setCustomerID(customerID);

        Order order1 = new Order();
        order1.setOrderID(1L);

        Order order2 = new Order();
        order2.setOrderID(2L);

        List<Order> orders = Arrays.asList(order1, order2);

        ObjectMapper objectMapper = new ObjectMapper();
        String ordersJson = objectMapper.writeValueAsString(orders);


        when(customerService.getCustomerByID(customerID)).thenReturn(Optional.of(customer));
        when(orderService.fetchOrderListByID(customerID)).thenReturn(orders);


        mockMvc.perform(get("/api/v1/target/{CustomerId}/getAllOrder", customerID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ordersJson));
    }

    @Test
    public void testFetchOrderListByCustomerID_CustomerNotFound() throws Exception {

        Long customerID = 1L;

        when(customerService.getCustomerByID(customerID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/target/{CustomerId}/getAllOrder", customerID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
