package com.targetindia.EcomStreaming.service;


public interface ProductService {
    public Long fetchProductStockLevel(Long ProductId);
    public void setProductStockLevel(Long ProductId,Long NewStockLevel);
}
