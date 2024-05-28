package com.targetindia.EcomStreaming.repository;
import com.targetindia.EcomStreaming.entites.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
}
