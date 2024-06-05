package com.targetindia.EcomStreaming.controllers;
import com.targetindia.EcomStreaming.exceptions.DatabaseConnError;
import com.targetindia.EcomStreaming.model.Product;
import com.targetindia.EcomStreaming.exceptions.InvalidCustomerId;
import com.targetindia.EcomStreaming.exceptions.InvalidProductId;
import com.targetindia.EcomStreaming.exceptions.InvalidProductQuantity;
import com.targetindia.EcomStreaming.service.OrderService;
import com.targetindia.EcomStreaming.service.ProductService;
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
@CrossOrigin("*")
public class OrderController {
    private final KafkaProducer kafkaProducer;
    public OrderController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @PostMapping("/order")
    public ResponseEntity<String> publish(@RequestBody Order order) {
        for(Product product: order.getProductList()){
            if(product.getProductQuantity() > productService.fetchProductStockLevel(product.getProductID())){
                throw new InvalidProductQuantity("Product Quantity only left:- "+productService.fetchProductStockLevel(product.getProductID()));
            }
        }
        for(Product product: order.getProductList()){
            Long OldStockLevel = productService.fetchProductStockLevel(product.getProductID());
            productService.setProductStockLevel(product.getProductID(),OldStockLevel- product.getProductQuantity());
        }
        kafkaProducer.sendMessage(order);
        return ResponseEntity.ok("Order is placed");
    }

    @GetMapping("/allOrders")
    public List<Order> fetchOrderList(){
            return orderService.fetchOrderList();

    }

    @GetMapping("order/{CustomerId}")
    public List<Order> fetchOrderListByCustomerID(@PathVariable("CustomerId") Long CustomerId){
            return orderService.fetchOrderListByID(CustomerId);
    }

}