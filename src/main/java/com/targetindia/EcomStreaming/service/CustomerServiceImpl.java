package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Address;
import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.exceptions.CustomerNotFoundException;
import com.targetindia.EcomStreaming.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Optional<Customer> getCustomerByID(Long customerID) {
        return Optional.ofNullable(customerRepository.findById(customerID)
                .orElseThrow(() -> new CustomerNotFoundException("User Not Found")));
    }

    @Override
    public Optional<Customer> getCustomerByUsername(String customerUsername) {
        return Optional.ofNullable(customerRepository.findByUsername(customerUsername)
                .orElseThrow(() -> new CustomerNotFoundException("User Not Found")));
    }

    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username).orElse(null);
    }

    @Override
    public String findCustomerPassword(Long CustomerID) {
        return customerRepository.getCustomerByID(CustomerID).getPassword();
    }

    @Override
    public boolean validateCustomer(String username, String password) {
        if(findByUsername(username)==null){
            return false;
        }
        return (Objects.equals(findByUsername(username).getPassword(), password));
    }

    @Override
    public boolean checkEmailExists(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Optional<Customer> checkUsernameExists(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public boolean checkPhoneNumberExists(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    public void createCustomer(String username, String email, String password, String firstName, String lastName, String phoneNumber, Address address) {
        if (username == null || email == null || password == null || firstName == null || lastName == null) {
            throw new IllegalArgumentException("Required fields are missing");
        }

        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);

        customerRepository.save(customer);
    }

    @Override
    public Customer save(Customer customer) {
        customerRepository.save(customer);
        return customer;
    }
}
