package com.targetindia.EcomStreaming.controllers;
import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.exceptions.CustomerIdException;
import com.targetindia.EcomStreaming.model.Product;
import com.targetindia.EcomStreaming.exceptions.ProductQuantityException;
//import com.targetindia.EcomStreaming.service.ArchivedOrdersService;
import com.targetindia.EcomStreaming.service.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.targetindia.EcomStreaming.entites.Order;

import java.util.List;
import java.util.Optional;


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

    @Autowired
    private CustomerService customerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/order")
    public ResponseEntity<String> publish(@RequestBody Order order) throws ProductQuantityException {
        for(Product product: order.getProductList()){
            Long productId = product.getProductID();
            Long productQuantity = product.getProductQuantity();
            Long productStockLevel = productService.fetchProductStockLevel(productId);

            if(productQuantity > productStockLevel){
                String message = "Product ID: " + productId + "'s quantity is more than stock level. Stock level left: " + productStockLevel;
                LOGGER.error(message);
                throw new ProductQuantityException(message);
            }
        }
        for(Product product: order.getProductList()){
            Long productId = product.getProductID();
            Long productQuantity = product.getProductQuantity();
            Long productStockLevel = productService.fetchProductStockLevel(productId);

            productService.setProductStockLevel(productId,productStockLevel- productQuantity);
            LOGGER.info("Product ID: " + productId + " with quantity: " + productQuantity + " added into order.");
        }
        kafkaProducer.sendMessage(order);
        return ResponseEntity.ok("Order is placed");
    }

    @GetMapping("/allOrders")
    public List<Order> fetchOrderList() {
        return orderService.fetchOrderList();
    }

    @GetMapping("order/{CustomerId}")
    public List<Order> fetchOrderListByCustomerID(@PathVariable("CustomerId") Long CustomerId) throws CustomerIdException {
        Optional<Customer> customer = customerService.getCustomerByID(CustomerId);
        if(customer.isEmpty()) {
            LOGGER.error("Invalid Customer ID: " + CustomerId);
            throw new CustomerIdException("Invalid Customer ID: " + CustomerId);
        }
        return orderService.fetchOrderListByID(CustomerId);
    }

}