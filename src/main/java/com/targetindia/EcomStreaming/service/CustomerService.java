package com.targetindia.EcomStreaming.service;


import com.targetindia.EcomStreaming.entites.Customer;

import java.util.Optional;

public interface CustomerService {
    public Optional<Customer> getCustomerByID(Long customerID);
}
