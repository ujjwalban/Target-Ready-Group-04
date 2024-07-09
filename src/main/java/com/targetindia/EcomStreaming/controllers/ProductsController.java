package com.targetindia.EcomStreaming.controllers;

import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/target")
@CrossOrigin("*")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @GetMapping("/allProducts")
    public List<Products> fetchProductsList(){
        return productService.displayAllProducts();
    }

    @GetMapping("/product/{productId}")
    public String getProductName(@PathVariable("productId") Long productId) {
        Optional<Products> product = productService.getProductByID(productId);
        return product.map(Products::getProductName).orElse("Product not found");
    }
}
