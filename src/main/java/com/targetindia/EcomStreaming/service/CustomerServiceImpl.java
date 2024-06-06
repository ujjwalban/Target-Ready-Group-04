package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.exceptions.CustomerNotFoundException;
import com.targetindia.EcomStreaming.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Optional<Customer> getCustomerByID(Long customerID) {
        return Optional.ofNullable(customerRepository.findById(customerID)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + customerID + " not found")));
    }
}
