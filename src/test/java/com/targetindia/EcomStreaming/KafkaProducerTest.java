package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import com.targetindia.EcomStreaming.service.ArchivedOrdersServiceImpl;
import com.targetindia.EcomStreaming.service.KafkaConsumer;
import com.targetindia.EcomStreaming.service.KafkaProducer;
import com.targetindia.EcomStreaming.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class KafkaProducerTest {

    @Autowired
    private KafkaProducer kafkaProducer;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private KafkaConsumer kafkaConsumer;
    @MockBean
    private OrderServiceImpl orderService;
    @MockBean
    private ArchivedOrdersServiceImpl archivedOrdersService;
    @MockBean
    private KafkaTemplate<String, Order> kafkaTemplate;
    @Test
    public void testSendMessage() {
        Order order = new Order();
        order.setCustomerID(1L);

        kafkaProducer.sendMessage(order);

        verify(kafkaTemplate, Mockito.times(1)).send((Message<?>) Mockito.any());
    }
}
