package com.targetindia.EcomStreaming.service;


import com.targetindia.EcomStreaming.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public Long fetchProductStockLevel(Long ProductId) {
        return productRepository.findById(ProductId).get().getStockLevel();
    }
    @Override
    public void setProductStockLevel(Long ProductId,Long NewStockLevel) {
        productRepository.findById(ProductId).get().setStockLevel(NewStockLevel);
    }
}
