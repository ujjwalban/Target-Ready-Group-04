package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Products;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    public Optional<Products> getProductByID(Long ProductID);
    public Long fetchProductStockLevel(Long ProductId);
    public void setProductStockLevel(Long ProductId,Long NewStockLevel);
    List<Products> displayAllProducts();
}
