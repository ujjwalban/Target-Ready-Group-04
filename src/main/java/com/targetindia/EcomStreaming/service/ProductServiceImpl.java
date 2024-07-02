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
    public Optional<Products> getProductByID(Long ProductID) {
        return Optional.ofNullable(productRepository.findById(ProductID)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found")));
    }

    @Override
    public Long fetchProductStockLevel(Long ProductId) throws ProductNotFoundException {
        try{
            return productRepository.findById(ProductId).get().getStockLevel();
        }
        catch (NoSuchElementException e){
            throw new ProductNotFoundException("Invalid Product ID:" + ProductId);
        }
    }
    @Override
    public void setProductStockLevel(Long ProductId, Long NewStockLevel){
        productRepository.findById(ProductId).get().setStockLevel(NewStockLevel);
    }

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackDisplayAllProducts")
    public List<Products> displayAllProducts(){
        throw new RuntimeException("Simulated service failure");
        //return productRepository.findAll();
    }

    public List<Products> fallbackDisplayAllProducts(Throwable throwable) {
        // Fallback logic if displayAllProducts fails
        System.out.println("Fallback called for displayAllProducts due to: " + throwable.getMessage());
        return List.of(); // or return an empty list or handle accordingly
    }
}
