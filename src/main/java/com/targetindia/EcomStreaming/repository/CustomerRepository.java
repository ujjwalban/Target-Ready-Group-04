package com.targetindia.EcomStreaming.repository;

import com.targetindia.EcomStreaming.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select c from Customer c where c.customerID = :customerID")
    public Customer getCustomerByID(@Param("customerID") Long customerID);

    @Query("select c from Customer c where c.username = :username")
    Optional<Customer> findByUsername(@Param("username") String username);

    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.email = :email")
    boolean findByEmail(@Param("email") String email);
    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.phoneNumber = :phoneNumber")
    boolean findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}
