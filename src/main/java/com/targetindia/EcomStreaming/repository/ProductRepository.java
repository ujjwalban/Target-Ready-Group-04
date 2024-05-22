package com.targetindia.EcomStreaming.repository;

import com.targetindia.EcomStreaming.entites.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
