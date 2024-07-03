package com.targetindia.EcomStreaming.controllers;

import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.exceptions.ProductNotFoundException;
import com.targetindia.EcomStreaming.model.Product;
import com.targetindia.EcomStreaming.exceptions.ProductQuantityException;
import com.targetindia.EcomStreaming.service.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import com.targetindia.EcomStreaming.entites.Order;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Transactional
@RestController
@RequestMapping("/api/v1/target")
@CrossOrigin("*")
public class OrderController {
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/order")
    public CompletableFuture<ResponseEntity<String>> publish(@RequestBody Order order) throws ProductQuantityException, ProductNotFoundException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                processOrder(order);
                return ResponseEntity.ok("Order is placed");
            } catch (ProductQuantityException | ProductNotFoundException e) {
                LOGGER.error("Order placement failed", e);
                throw new RuntimeException(e);
            }
        });
    }

    @Async("asyncExecutor")
    private void processOrder(Order order) throws ProductQuantityException, ProductNotFoundException {
        Optional<Customer> customer = customerService.getCustomerByUsername(order.getCustomerUsername());
        order.setCustomerID(customerService.findByUsername(order.getCustomerUsername()).getCustomerID());

        List<CompletableFuture<Void>> validationFutures = order.getProductList().stream()
                .map(this::validateProductStock)
                .toList();

        CompletableFuture.allOf(validationFutures.toArray(new CompletableFuture[0])).join();

        List<CompletableFuture<Void>> updateFutures = order.getProductList().stream()
                .map(product -> updateProductStock(product, order))
                .toList();

        try {
            CompletableFuture.allOf(updateFutures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            LOGGER.error("Stock update failed", e);
            rollbackStockUpdates(order);
            throw new RuntimeException("Order placement failed. Stock levels have been rolled back.");
        }

        kafkaProducer.sendMessage(order);
    }

    @Async("asyncExecutor")
    private CompletableFuture<Void> validateProductStock(Product product) {
        return CompletableFuture.runAsync(() -> {
            Long productId = product.getProductID();
            Long productQuantity = product.getProductQuantity();
            Long productStockLevel = productService.fetchProductStockLevel(productId);

            if (productQuantity > productStockLevel) {
                String message = "Product ID: " + productId + "'s quantity is more than stock level. Stock level left: " + productStockLevel;
                LOGGER.error(message);
                throw new ProductQuantityException(message);
            }
        });
    }

    @Async("asyncExecutor")
    private CompletableFuture<Void> updateProductStock(Product product, Order order) {
        return CompletableFuture.runAsync(() -> {
            Long productId = product.getProductID();
            Long productQuantity = product.getProductQuantity();
            Long productStockLevel = productService.fetchProductStockLevel(productId);

            productService.setProductStockLevel(productId, productStockLevel - productQuantity);
            LOGGER.info("Product ID: " + productId + " with quantity: " + productQuantity + " added into order: " + order.getOrderID());
        });
    }

    private void rollbackStockUpdates(Order order) {
        order.getProductList().forEach(product -> {
            Long productId = product.getProductID();
            Long productQuantity = product.getProductQuantity();
            Long productStockLevel = productService.fetchProductStockLevel(productId);

            productService.setProductStockLevel(productId, productStockLevel + productQuantity);
            LOGGER.info("Rolled back Product ID: " + productId + " with quantity: " + productQuantity + " for order: " + order.getOrderID());
        });
    }
}
