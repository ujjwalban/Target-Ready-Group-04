package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.controllers.CustomerController;
import com.targetindia.EcomStreaming.entites.Address;
import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.entites.Order;
import com.targetindia.EcomStreaming.exceptions.CustomerNotFoundException;
import com.targetindia.EcomStreaming.service.CustomerAddressService;
import com.targetindia.EcomStreaming.service.CustomerService;
import com.targetindia.EcomStreaming.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerAddressService customerAddressService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchOrderListByCustomerID() throws CustomerNotFoundException {
        // Mock data
        String username = "testUser";
        Customer customer = new Customer();
        customer.setUsername(username);
        List<Order> orders = Arrays.asList(new Order(), new Order());

        // Mock service method behavior
        when(customerService.getCustomerByUsername(username)).thenReturn(Optional.of(customer));
        when(orderService.fetchOrderListByUsername(username)).thenReturn(orders);

        // Call controller method
        List<Order> result = customerController.fetchOrderListByCustomerID(username);

        // Verify
        assertEquals(orders.size(), result.size());
        verify(customerService, times(1)).getCustomerByUsername(username);
        verify(orderService, times(1)).fetchOrderListByUsername(username);
    }

    @Test
    void testValidateCustomerCredentials_ValidCredentials() {
        // Mock data
        String username = "testUser";
        String password = "password";
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        // Mock service method behavior
        when(customerService.findByUsername(username)).thenReturn(customer);
        when(customerService.validateCustomer(username, password)).thenReturn(true);

        // Call controller method
        ResponseEntity<String> response = customerController.validateCustomerCredentials(credentials);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully Login", response.getBody());
        verify(customerService, times(1)).findByUsername(username);
        verify(customerService, times(1)).validateCustomer(username, password);
    }

    @Test
    void testValidateCustomerCredentials_InvalidCredentials() {
        // Mock data
        String username = "testUser";
        String password = "wrongPassword";

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        // Mock service method behavior
        when(customerService.findByUsername(username)).thenReturn(new Customer());
        when(customerService.validateCustomer(username, password)).thenReturn(false);

        // Call controller method
        ResponseEntity<String> response = customerController.validateCustomerCredentials(credentials);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
        verify(customerService, times(1)).findByUsername(username);
        verify(customerService, times(1)).validateCustomer(username, password);
    }

    @Test
    void testValidateCustomerCredentials_UsernameNotFound() {
        // Mock data
        String username = "nonExistingUser";

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);

        // Mock service method behavior
        when(customerService.findByUsername(username)).thenReturn(null);

        // Call controller method
        ResponseEntity<String> response = customerController.validateCustomerCredentials(credentials);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Username not found", response.getBody());
        verify(customerService, times(1)).findByUsername(username);
        verify(customerService, never()).validateCustomer(anyString(), anyString());
    }

    @Test
    void testRateLimiterFallback() {
        // Mock data
        Map<String, String> credentials = new HashMap<>();

        // Call fallback method directly
        ResponseEntity<String> response = customerController.rateLimiterFallback(credentials, new RuntimeException());

        // Verify
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertEquals("Too many requests, please try again later.", response.getBody());
    }

    @Test
    void testUpdateAddress_Success() {
        // Mock data
        String username = "testUser";
        Address address = new Address();
        address.setStreet("123 Main St");

        Customer existingCustomer = new Customer();
        existingCustomer.setUsername(username);

        // Mock service method behavior
        when(customerService.findByUsername(username)).thenReturn(existingCustomer);
        when(customerService.save(existingCustomer)).thenReturn(existingCustomer);

        // Call controller method
        Customer updatedCustomer = customerController.updateAddress(username, address);

        // Verify
        assertEquals(address.getStreet(), updatedCustomer.getAddress().getStreet());
        verify(customerService, times(1)).findByUsername(username);
        verify(customerService, times(1)).save(existingCustomer);
    }

    @Test
    void testUpdateAddress_CustomerNotFound() {
        // Mock data
        String username = "nonExistingUser";
        Address address = new Address();
        address.setStreet("123 Main St");

        // Mock service method behavior
        when(customerService.findByUsername(username)).thenReturn(null);

        // Call controller method and expect exception
        assertThrows(RuntimeException.class, () -> customerController.updateAddress(username, address));

        // Verify
        verify(customerService, times(1)).findByUsername(username);
        verify(customerService, never()).save(any());
    }

    @Test
    void testGetCustomerIDByUsername() {
        // Mock data
        String username = "testUser";
        Long customerId = 1L;

        // Mock service method behavior
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setCustomerID(customerId);
        when(customerService.findByUsername(username)).thenReturn(customer);

        // Call controller method
        Long result = customerController.getCustomerIDByUsername(username);

        // Verify
        assertEquals(customerId, result);
        verify(customerService, times(1)).findByUsername(username);
    }
}
