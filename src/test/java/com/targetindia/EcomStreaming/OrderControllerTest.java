package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.controllers.OrderController;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.model.Product;
import com.targetindia.EcomStreaming.service.KafkaProducer;
import com.targetindia.EcomStreaming.service.OrderService;
import com.targetindia.EcomStreaming.service.ProductService;
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
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @Test
    public void testPublishOrder() throws Exception {
        // Arrange
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

        mockMvc.perform(post("/api/v1/target/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerID\": 1, \"productList\": [{\"productID\": 1, \"productQuantity\": 2}]}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPublishOrder_ProductQuantityException() throws Exception {
        // Arrange
        Order order = new Order();
        order.setCustomerID(1L);

        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setProductID(1L);
        product.setProductQuantity(12L);  // Quantity more than stock level
        productList.add(product);

        order.setProductList(productList);

        Mockito.when(productService.fetchProductStockLevel(1L)).thenReturn(10L);

        mockMvc.perform(post("/api/v1/target/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerID\": 1, \"productList\": [{\"productID\": 1, \"productQuantity\": 12}]}"))
                .andExpect(status().isInternalServerError());
    }
}
