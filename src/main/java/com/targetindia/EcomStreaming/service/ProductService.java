package com.targetindia.EcomStreaming.service;


import com.targetindia.EcomStreaming.entites.Products;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductService {
    public CompletableFuture<Long> fetchProductStockLevel(Long ProductId);
    public void setProductStockLevel(Long ProductId,Long NewStockLevel);
    List<Products> displayAllProducts();
}
