package com.targetindia.EcomStreaming.controllers;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.service.KafkaProducer;


@Transactional
@RestController
@RequestMapping("/api/v1/target")
public class OrderController {
    private final KafkaProducer kafkaProducer;
    public OrderController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/order")
    public ResponseEntity<String> publish(@RequestBody Order order) {
        kafkaProducer.sendMessage(order);
        return ResponseEntity.ok("Order is placed");
    }
}