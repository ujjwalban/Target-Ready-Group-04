package com.targetindia.EcomStreaming.repository;
import com.targetindia.EcomStreaming.entites.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OrderRepository extends JpaRepository<Order, Long> {
}
