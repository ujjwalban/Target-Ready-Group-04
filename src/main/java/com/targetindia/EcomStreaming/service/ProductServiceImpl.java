package com.targetindia.EcomStreaming.service;


import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.exceptions.ProductIdException;
import com.targetindia.EcomStreaming.exceptions.StockLevelException;
import com.targetindia.EcomStreaming.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public Long fetchProductStockLevel(Long ProductId) throws ProductIdException {
        if(!(1 <= ProductId && ProductId <= 96)) {
            throw new ProductIdException("Invalid Product ID:" + ProductId);
        }
        return productRepository.findById(ProductId).get().getStockLevel();
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
