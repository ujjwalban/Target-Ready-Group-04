package com.targetindia.EcomStreaming.service;


import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.entites.Products;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public Optional<Products> getProductByID(Long ProductID);
    public Long fetchProductStockLevel(Long ProductId);
    public void setProductStockLevel(Long ProductId,Long NewStockLevel);
    List<Products> displayAllProducts();
}
