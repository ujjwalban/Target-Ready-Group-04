package com.targetindia.EcomStreaming.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.kafka.KafkaProducer;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/target")
public class OrderController {

    private final KafkaProducer kafkaProducer;
    
    public OrderController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }
 
    @PostMapping("/order")
    public ResponseEntity<String> publish(@RequestBody Order order) {
        order.setOrderID(UUID.randomUUID());
        kafkaProducer.sendMessage(order);
        return ResponseEntity.ok("Order is placed");
    }
}