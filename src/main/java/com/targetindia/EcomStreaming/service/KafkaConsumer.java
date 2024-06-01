package com.targetindia.EcomStreaming.service;

//import com.targetindia.EcomStreaming.entites.Customer;
//import com.targetindia.EcomStreaming.model.Product;
//import com.targetindia.EcomStreaming.repository.CustomerRepository;
import com.targetindia.EcomStreaming.repository.OrderRepository;
//import com.targetindia.EcomStreaming.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.targetindia.EcomStreaming.entites.Order;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    @Autowired
    private OrderRepository orderRepository;

    @KafkaListener(topics = "Orders", groupId = "myGroup", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void listen(Order order) {
        LOGGER.info(String.format("Message Recived -> %s", order.toString()));
        orderRepository.save(order);
    }


}
