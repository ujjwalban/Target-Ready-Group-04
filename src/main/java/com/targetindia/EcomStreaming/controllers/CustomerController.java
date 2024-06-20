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

    @GetMapping("/{CustomerUsername}/getAllOrder")
    public List<Order> fetchOrderListByCustomerID(@PathVariable("CustomerUsername") String customerUsername) throws CustomerNotFoundException {
        Optional<Customer> customer = customerService.getCustomerByUsername(customerUsername);
        return orderService.fetchOrderListByUsername(customerUsername);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> validateCustomerCredentials(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        System.out.println(username);
        System.out.println(password);

        if (customerService.findByUsername(username) == null) {
            return ResponseEntity.ok("Username not found");
        }
        if (customerService.validateCustomer(username, password)) {
            return ResponseEntity.ok("Successfully Login");
        }
        return ResponseEntity.ok("Invalid credentials");
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<String> saveCustomer(@RequestBody Customer customer){
        System.out.println(customer.getFirstName());
        System.out.println(customer);
        if(customerService.findByUsername(customer.getUsername())!=null){
            ResponseEntity.ok("Username already exists");
        }
        if(customerService.checkEmailExists(customer.getEmail())){
            ResponseEntity.ok("Email already exists");
        }
        if(customerService.checkPhoneNumberExists(customer.getPhoneNumber())){
            ResponseEntity.ok("PhoneNumber already exists");
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
