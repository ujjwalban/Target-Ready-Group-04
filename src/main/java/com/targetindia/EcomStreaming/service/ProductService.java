package com.targetindia.EcomStreaming.service;


import com.targetindia.EcomStreaming.entites.Products;

import java.util.List;

public interface ProductService {
    public Long fetchProductStockLevel(Long ProductId);
    public void setProductStockLevel(Long ProductId,Long NewStockLevel);
    List<Products> displayAllProducts();
}
