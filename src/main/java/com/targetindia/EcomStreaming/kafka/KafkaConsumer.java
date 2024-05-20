package com.targetindia.EcomStreaming.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.targetindia.EcomStreaming.entites.Orders;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    
    @KafkaListener(topics = "Orders", groupId = "myGroup")
    public void consume(Orders order) {
        LOGGER.info(String.format("Message Recived -> %s", order.toString()));
    }
}
