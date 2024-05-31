package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import com.targetindia.EcomStreaming.service.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class KafkaConsumerTest {

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void testListen() {
        Order order = new Order();
        order.setCustomerID(1L);

        kafkaConsumer.listen(order);

        verify(orderRepository, Mockito.times(1)).save(order);
    }
}
