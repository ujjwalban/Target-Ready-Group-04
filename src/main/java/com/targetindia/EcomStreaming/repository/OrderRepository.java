package com.targetindia.EcomStreaming.repository;
import com.targetindia.EcomStreaming.entites.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface OrderRepository extends JpaRepository<Order, UUID> {
}
