package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Address;
import com.targetindia.EcomStreaming.entites.Customer;

import java.util.Optional;

public interface CustomerService {
    public Optional<Customer> getCustomerByID(Long customerID);
    public Optional<Customer> getCustomerByUsername(String CustomerUsername);
    public Customer findByUsername(String username);

    public String findCustomerPassword(Long CustomerID);

    public boolean validateCustomer(String username,String password);

    public boolean checkEmailExists(String email);

    public Optional<Customer> checkUsernameExists(String username);

    public boolean checkPhoneNumberExists(String phoneNumber);

    public void createCustomer(String username, String email, String password, String firstName, String lastName, String phoneNumber, Address address);

    public Customer save(Customer customer);
}
