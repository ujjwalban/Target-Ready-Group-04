package com.targetindia.EcomStreaming.repository;

import com.targetindia.EcomStreaming.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
