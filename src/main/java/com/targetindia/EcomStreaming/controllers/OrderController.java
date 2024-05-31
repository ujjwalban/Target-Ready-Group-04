package com.targetindia.EcomStreaming.controllers;
import com.targetindia.EcomStreaming.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.service.KafkaProducer;

import java.util.List;


@Transactional
@RestController
@RequestMapping("/api/v1/target")
public class OrderController {
    private final KafkaProducer kafkaProducer;
    public OrderController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<String> publish(@RequestBody Order order) {
        kafkaProducer.sendMessage(order);
        return ResponseEntity.ok("Order is placed");
    }

    @GetMapping("/allOrders")
    public List<Order> fetchOrderList(){
        return orderService.fetchOrderList();
    }

}