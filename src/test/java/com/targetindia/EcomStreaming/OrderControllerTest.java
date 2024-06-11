package com.targetindia.EcomStreaming;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targetindia.EcomStreaming.controllers.OrderController;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.model.Product;
import com.targetindia.EcomStreaming.service.CustomerService;
import com.targetindia.EcomStreaming.service.KafkaProducer;
import com.targetindia.EcomStreaming.service.OrderService;
import com.targetindia.EcomStreaming.service.ProductService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaProducer kafkaProducer;

    @MockBean
    private ProductService productService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private EntityManager entityManager;

    @Test
    public void testPublishOrder() throws Exception {
        Order order = new Order();
        order.setCustomerID(1L);

        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setProductID(1L);
        product.setProductQuantity(2L);
        productList.add(product);

        order.setProductList(productList);

        Mockito.when(productService.fetchProductStockLevel(1L)).thenReturn(10L);
        Mockito.doNothing().when(productService).setProductStockLevel(1L, 8L);

        Mockito.doNothing().when(kafkaProducer).sendMessage(Mockito.any(Order.class));

        ObjectMapper objectMapper = new ObjectMapper();
        String orderJson = objectMapper.writeValueAsString(order);

        mockMvc.perform(post("/api/v1/target/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testPublishOrder_ProductQuantityException() throws Exception {
        Order order = new Order();
        order.setCustomerID(1L);

        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setProductID(1L);
        product.setProductQuantity(12L);
        productList.add(product);

        order.setProductList(productList);

        Mockito.when(productService.fetchProductStockLevel(1L)).thenReturn(10L);

        ObjectMapper objectMapper = new ObjectMapper();
        String orderJson = objectMapper.writeValueAsString(order);

        mockMvc.perform(post("/api/v1/target/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isBadRequest());
    }
}
