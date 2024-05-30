package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.configs.KafkaTopicConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class KafkaTopicConfigTest {

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    @Test
    public void testOrdersTopic() {
        NewTopic topic = kafkaTopicConfig.Orders();
        assertThat(topic.name()).isEqualTo("Orders");
    }
}
