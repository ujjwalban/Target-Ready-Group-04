package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Address;
import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.exceptions.CustomerNotFoundException;
import com.targetindia.EcomStreaming.repository.CustomerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name = "customerService", fallbackMethod = "fallbackGetCustomerByID")
    public Optional<Customer> getCustomerByID(Long customerID) {
        return Optional.ofNullable(customerRepository.findById(customerID)
                .orElseThrow(() -> new CustomerNotFoundException("User Not Found")));
    }
    public Optional<Customer> fallbackGetCustomerByID(Long customerID, Throwable throwable) {
        // Log the exception or handle it as needed
        System.out.println("Fallback method triggered due to: " + throwable.getMessage());

        // Return an empty Optional or a default value
        return Optional.empty();
    }

    @Override
    @CircuitBreaker(name = "customerService", fallbackMethod = "fallbackGetCustomerByUsername")
    public Optional<Customer> getCustomerByUsername(String customerUsername) {
        return Optional.ofNullable(customerRepository.findByUsername(customerUsername)
                .orElseThrow(() -> new CustomerNotFoundException("User Not Found")));
    }
    public Optional<Customer> fallbackGetCustomerByUsername(String customerUsername, Throwable throwable) {
        // Log the exception or handle it as needed
        System.out.println("Fallback method triggered due to: " + throwable.getMessage());

        // Return an empty Optional or a default value
        return Optional.empty();
    }

    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username).orElse(null);
    }


    @Override
    @CircuitBreaker(name = "customerService", fallbackMethod = "fallbackFindCustomerPassword")
    public String findCustomerPassword(Long CustomerID) {
        return customerRepository.getCustomerByID(CustomerID).getPassword();
    }
    public String fallbackFindCustomerPassword(Long customerID, Throwable throwable) {
        // Log the exception or handle it as needed
        System.out.println("Fallback method triggered due to: " + throwable.getMessage());

        // Return a default password or handle the error gracefully
        return "";
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

    @CircuitBreaker(name = "customerService", fallbackMethod = "fallbackCreateCustomer")
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
    public void fallbackCreateCustomer(String username, String email, String password, String firstName, String lastName, String phoneNumber, Address address, Throwable throwable) {
        // Log the exception or handle it as needed
        System.out.println("Fallback method triggered due to: " + throwable.getMessage());

        // Implement fallback logic, such as logging the failed creation attempt
    }

    @Override
    @CircuitBreaker(name = "customerService", fallbackMethod = "fallbackSave")
    public Customer save(Customer customer) {
        customerRepository.save(customer);
        return customer;
    }
    public Customer fallbackSave(Customer customer, Throwable throwable) {
        // Log the exception or handle it as needed
        System.out.println("Fallback method triggered due to: " + throwable.getMessage());

        // Return the customer or implement fallback logic
        return customer;
    }
}
