package com.targetindia.EcomStreaming.controllers;

import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.exceptions.CustomerIdException;
import com.targetindia.EcomStreaming.exceptions.CustomerNotFoundException;
import com.targetindia.EcomStreaming.service.CustomerService;
import com.targetindia.EcomStreaming.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/target")
public class CustomerController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("{CustomerId}/getAllOrder")
    public List<Order> fetchOrderListByCustomerID(@PathVariable("CustomerId") Long CustomerID) throws CustomerIdException, CustomerNotFoundException {
        Optional<Customer> customer = customerService.getCustomerByID(CustomerID);
        return orderService.fetchOrderListByID(CustomerID);
    }
}
