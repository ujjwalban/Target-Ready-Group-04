package com.targetindia.EcomStreaming.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.targetindia.EcomStreaming.entites.Orders;
import com.targetindia.EcomStreaming.kafka.KafkaProducer;


@RestController
@RequestMapping("/api/v1/target")
public class OrderController {

    private KafkaProducer kafkaProducer;
    
    public OrderController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }
 
    @PostMapping("/order")
    public ResponseEntity<String> publish(@RequestBody Orders order) {
        kafkaProducer.sendMessage(order);
        return ResponseEntity.ok("Order is placed");
    }
}