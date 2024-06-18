package com.targetindia.EcomStreaming.controllers;

import com.targetindia.EcomStreaming.entites.Address;
import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.exceptions.CustomerNotFoundException;
import com.targetindia.EcomStreaming.repository.AddressRepository;
import com.targetindia.EcomStreaming.service.CustomerAddressService;
import com.targetindia.EcomStreaming.service.CustomerService;
import com.targetindia.EcomStreaming.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/target")
@CrossOrigin("*")
public class CustomerController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerAddressService customerAddressService;

    @GetMapping("/{CustomerId}/getAllOrder")
    public List<Order> fetchOrderListByCustomerID(@PathVariable("CustomerId") Long CustomerID) throws CustomerNotFoundException {
        Optional<Customer> customer = customerService.getCustomerByID(CustomerID);
        return orderService.fetchOrderListByID(CustomerID);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> validateCustomerCredentials(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (customerService.validateCustomer(username, password)) {
            return ResponseEntity.ok("Successfully Login");
        }
        if (customerService.findByUsername(username) == null) {
            return ResponseEntity.ok("Username not found");
        }
        return ResponseEntity.ok("Invalid credentials");
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<String> saveCustomer(@RequestBody Customer customer){
        System.out.println(customer.getFirstName());
        System.out.println(customer);
        if(customerService.findByUsername(customer.getUsername())!=null){
            ResponseEntity.ofNullable("Username already exists");
            throw new IllegalArgumentException("Username is taken");
        }
        if(customerService.checkEmailExists(customer.getEmail())){
            ResponseEntity.ofNullable("Email already exists");
            throw new IllegalArgumentException("Username is taken");
        }
        if(customerService.checkPhoneNumberExists(customer.getPhoneNumber())){
            ResponseEntity.ofNullable("PhoneNumber already exists");
            throw new IllegalArgumentException("PhoneNumber is taken");
        }
        customerService.createCustomer(customer.getUsername(),customer.getEmail(), customer.getPassword(), customer.getFirstName(), customer.getLastName(), customer.getPhoneNumber(),customer.getAddress());
        return ResponseEntity.ok("Successfully Signed up");
    }

    @PutMapping("/{username}/address")
    public Customer updateAddress(@PathVariable("username") String username, @RequestBody Address address) {
        Optional<Customer> optionalCustomer = Optional.ofNullable(customerService.findByUsername(username));
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setAddress(address);
            return customerService.save(customer);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    @GetMapping("/{Username}/getId")
    public Long getCustomerIDByUsername(@PathVariable String Username){
       return customerService.findByUsername(Username).getCustomerID();
    }
}
