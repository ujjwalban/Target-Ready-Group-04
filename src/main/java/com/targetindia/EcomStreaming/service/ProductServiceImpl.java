package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.exceptions.ProductNotFoundException;
import com.targetindia.EcomStreaming.repository.ProductRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackGetProductByID")
    public Optional<Products> getProductByID(Long ProductID) {
        return Optional.ofNullable(productRepository.findById(ProductID)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found")));
    }

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackFetchProductStockLevel")
    public Long fetchProductStockLevel(Long ProductId) throws ProductNotFoundException {
        try{
            return productRepository.findById(ProductId).get().getStockLevel();
        }
        catch (NoSuchElementException e){
            throw new ProductNotFoundException("Invalid Product ID:" + ProductId);
        }
    }

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackSetProductStockLevel")
    public void setProductStockLevel(Long ProductId, Long NewStockLevel){
        productRepository.findById(ProductId).get().setStockLevel(NewStockLevel);
    }

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackDisplayAllProducts")
    public List<Products> displayAllProducts(){
        //throw new RuntimeException("Simulated service failure");
        return productRepository.findAll();
    }

    public List<Products> fallbackDisplayAllProducts(Throwable throwable) {
        // Fallback logic if displayAllProducts fails
        System.out.println("Fallback called for displayAllProducts due to: " + throwable.getMessage());
        return List.of(); // or return an empty list or handle accordingly
    }

    public Optional<Products> fallbackGetProductByID(Long ProductID, Throwable throwable) {
        // Log the exception or handle it as needed
        System.out.println("Fallback method triggered due to: " + throwable.getMessage());
        // Return an optional empty or a default value
        return Optional.empty();
    }

    public Long fallbackFetchProductStockLevel(Long productId, Throwable throwable) {
        // Log the exception or handle it as needed
        System.out.println("Fallback method triggered due to: " + throwable.getMessage());

        // Return a default stock level value, e.g., 0L
        return 0L;
    }

    public void fallbackSetProductStockLevel(Long productId, Long newStockLevel, Throwable throwable) {
        // Log the exception or handle it as needed
        System.out.println("Fallback method triggered due to: " + throwable.getMessage());

        // Optionally log the new stock level and product ID for later processing
        System.out.println("Failed to set stock level for Product ID: " + productId + " to " + newStockLevel);
 }
}
