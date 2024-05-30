package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.controllers.OrderController;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.service.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaProducer kafkaProducer;

    @Test
    public void testPublishOrder() throws Exception {
        Order order = new Order();
        order.setCustomerID(1L);

        Mockito.doNothing().when(kafkaProducer).sendMessage(Mockito.any(Order.class));

        mockMvc.perform(post("/api/v1/target/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerID\": 1, \"productList\": []}"))
                .andExpect(status().isOk());
    }
}
