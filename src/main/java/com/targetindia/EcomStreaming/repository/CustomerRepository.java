package com.targetindia.EcomStreaming.repository;

import com.targetindia.EcomStreaming.entites.Customer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select c from Customer c where c.customerID = :customerID")
    public Customer getCustomerByID(@Param("customerID") Long customerID);
}
