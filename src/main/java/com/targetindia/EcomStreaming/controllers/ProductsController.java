package com.targetindia.EcomStreaming.controllers;

import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/target")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @GetMapping("/allProducts")
    public List<Products> fetchProductsList(){
        return productService.displayAllProducts();
    }
}
