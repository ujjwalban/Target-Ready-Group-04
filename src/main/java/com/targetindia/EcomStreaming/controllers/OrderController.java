package com.targetindia.EcomStreaming.controllers;

import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.exceptions.CustomerIdException;
import com.targetindia.EcomStreaming.exceptions.ProductQuantityException;
import com.targetindia.EcomStreaming.model.Product;
import com.targetindia.EcomStreaming.service.CustomerService;
import com.targetindia.EcomStreaming.service.KafkaProducer;
import com.targetindia.EcomStreaming.service.OrderService;
import com.targetindia.EcomStreaming.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/target")
@CrossOrigin("*")
public class OrderController {
    private final KafkaProducer kafkaProducer;
    private final OrderService orderService;
    private final ProductService productService;
    private final CustomerService customerService;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(KafkaProducer kafkaProducer, OrderService orderService, ProductService productService, CustomerService customerService) {
        this.kafkaProducer = kafkaProducer;
        this.orderService = orderService;
        this.productService = productService;
        this.customerService = customerService;
    }

    @PostMapping("/order")
    public ResponseEntity<String> publish(@RequestBody Order order) throws ProductQuantityException {
        List<Product> products = order.getProductList();

        // Validate product quantities asynchronously
        List<CompletableFuture<Void>> validations = products.stream()
                .map(product -> {
                    Long productId = product.getProductID();
                    Long productQuantity = product.getProductQuantity();
                    return productService.fetchProductStockLevel(productId)
                            .thenAccept(productStockLevel -> {
                                if (productQuantity > productStockLevel) {
                                    String message = "Product ID: " + productId + "'s quantity is more than stock level. Stock level left: " + productStockLevel;
                                    LOGGER.error(message);
                                    throw new ProductQuantityException(message);
                                }
                            });
                })
                .toList();

        // Wait for all validations to complete
        CompletableFuture<Void> allValidations = CompletableFuture.allOf(
                validations.toArray(new CompletableFuture[0]));

        // Update product stock levels asynchronously
        CompletableFuture<Void> updateStockLevels = allValidations.thenCompose(voidResult -> {
            List<CompletableFuture<Void>> updates = products.stream()
                    .map(product -> {
                        Long productId = product.getProductID();
                        Long productQuantity = product.getProductQuantity();
                        return productService.fetchProductStockLevel(productId)
                                .thenAccept(productStockLevel -> {
                                    productService.setProductStockLevel(productId, productStockLevel - productQuantity);
                                    LOGGER.info("Product ID: " + productId + " with quantity: " + productQuantity + " added into order: " + order.getOrderID());
                                });
                    })
                    .toList();

            return CompletableFuture.allOf(updates.toArray(new CompletableFuture[0]));
        });

        // Send order to Kafka asynchronously after stock updates
        CompletableFuture<Void> kafkaSend = updateStockLevels.thenAccept(voidResult -> {
            kafkaProducer.sendMessage(order);
        });

        // Wait for Kafka message send to complete
        kafkaSend.join(); // Blocking wait to ensure response is sent only after Kafka send completes

        return ResponseEntity.ok("Order " + order.getOrderID() + " is placed");
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

    @PostMapping(value = "/placeAsyncOrders")
    public ResponseEntity<String> publishAsyncOrders(@RequestBody List<Order> orders) {
        List<CompletableFuture<Void>> futures = orders.stream()
                .map(order -> CompletableFuture.runAsync(() -> {
                    try {
                        publish(order);
                    } catch (ProductQuantityException e) {
                        LOGGER.error("Failed to process order ID: " + order.getOrderID(), e);
                    }
                }))
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join();
        return ResponseEntity.ok("All orders have been processed asynchronously.");
    }
}
