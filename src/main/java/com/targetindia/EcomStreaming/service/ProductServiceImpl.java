package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.exceptions.ProductIdException;
import com.targetindia.EcomStreaming.exceptions.StockLevelException;
import com.targetindia.EcomStreaming.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Async
    @Override
    public CompletableFuture<Long> fetchProductStockLevel(Long productId) {
        try {
            Long stockLevel = fetchProductStockLevelFromDatabase(productId);
            return CompletableFuture.completedFuture(stockLevel);
        } catch (ProductIdException e) {
            CompletableFuture<Long> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    private Long fetchProductStockLevelFromDatabase(Long productId) throws ProductIdException {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductIdException("Product with ID " + productId + " not found"));
        return product.getStockLevel();
    }

    @Override
    public void setProductStockLevel(Long ProductId, Long NewStockLevel) throws StockLevelException {
        if(NewStockLevel <= 0) {
            throw new StockLevelException("Invalid Stock Level: " + NewStockLevel);
        }
        productRepository.findById(ProductId).get().setStockLevel(NewStockLevel);
    }

    @Override
    public List<Products> displayAllProducts(){
        return productRepository.findAll();
    }
}
