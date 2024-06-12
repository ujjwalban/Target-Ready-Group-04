package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.configs.KafkaTopicConfig;
import com.targetindia.EcomStreaming.controllers.CustomerController;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import com.targetindia.EcomStreaming.service.ArchivedOrdersServiceImpl;
import com.targetindia.EcomStreaming.service.KafkaConsumer;
import com.targetindia.EcomStreaming.service.OrderServiceImpl;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class KafkaTopicConfigTest {

    @MockBean
    private CustomerController customerController;
    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    @MockBean
    private OrderServiceImpl orderService;
    @MockBean
    private KafkaConsumer kafkaConsumer;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private ArchivedOrdersServiceImpl archivedOrdersService;
    @Test
    public void testOrdersTopic() {
        NewTopic topic = kafkaTopicConfig.Orders();
        assertThat(topic.name()).isEqualTo("Orders");
    }
}
