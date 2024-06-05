package com.targetindia.EcomStreaming.service;


import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public Long fetchProductStockLevel(Long ProductId) {
        try{
            return productRepository.findById(ProductId).get().getStockLevel();
        }
        catch (NullPointerException e){
            throw new NullPointerException("Invalid ID");
        }
    }
    @Override
    public void setProductStockLevel(Long ProductId,Long NewStockLevel) {
        productRepository.findById(ProductId).get().setStockLevel(NewStockLevel);
    }

    @Override
    public List<Products> displayAllProducts(){
        return productRepository.findAll();
    }
}
