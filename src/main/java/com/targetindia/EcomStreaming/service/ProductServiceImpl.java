package com.targetindia.EcomStreaming.service;


import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.exceptions.CustomerNotFoundException;
import com.targetindia.EcomStreaming.exceptions.ProductIdException;
import com.targetindia.EcomStreaming.exceptions.ProductNotFoundException;
import com.targetindia.EcomStreaming.exceptions.StockLevelException;
import com.targetindia.EcomStreaming.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Long fetchProductStockLevel(Long ProductId) throws ProductIdException {
        try{
            return productRepository.findById(ProductId).get().getStockLevel();
        }
        catch (ProductIdException e){
            throw new ProductIdException("Invalid Product ID:" + ProductId);
        }
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
