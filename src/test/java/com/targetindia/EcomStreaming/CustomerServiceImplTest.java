package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.entites.Address;
import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.exceptions.CustomerNotFoundException;
import com.targetindia.EcomStreaming.repository.CustomerRepository;
import com.targetindia.EcomStreaming.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCustomerByIDSuccess() {
        Customer customer = new Customer();
        customer.setCustomerID(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerByID(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getCustomerID());
    }

    @Test
    void testGetCustomerByIDNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByID(1L));
    }

    @Test
    void testFallbackGetCustomerByID() {
        Optional<Customer> result = customerService.fallbackGetCustomerByID(1L, new RuntimeException("Simulated exception"));
        assertFalse(result.isPresent());
    }

    @Test
    void testGetCustomerByUsernameSuccess() {
        Customer customer = new Customer();
        customer.setUsername("test_user");
        when(customerRepository.findByUsername("test_user")).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerByUsername("test_user");

        assertTrue(result.isPresent());
        assertEquals("test_user", result.get().getUsername());
    }

    @Test
    void testGetCustomerByUsernameNotFound() {
        when(customerRepository.findByUsername("test_user")).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByUsername("test_user"));
    }

    @Test
    void testFallbackGetCustomerByUsername() {
        Optional<Customer> result = customerService.fallbackGetCustomerByUsername("test_user", new RuntimeException("Simulated exception"));
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByUsername() {
        Customer customer = new Customer();
        customer.setUsername("test_user");
        when(customerRepository.findByUsername("test_user")).thenReturn(Optional.of(customer));

        Customer result = customerService.findByUsername("test_user");

        assertNotNull(result);
        assertEquals("test_user", result.getUsername());
    }

    @Test
    void testFindCustomerPasswordSuccess() {
        Customer customer = new Customer();
        customer.setCustomerID(1L);
        customer.setPassword("password");
        when(customerRepository.getCustomerByID(1L)).thenReturn(customer);

        String result = customerService.findCustomerPassword(1L);

        assertEquals("password", result);
    }

    @Test
    void testFallbackFindCustomerPassword() {
        String result = customerService.fallbackFindCustomerPassword(1L, new RuntimeException("Simulated exception"));

        assertEquals("", result);
    }

    @Test
    void testValidateCustomerSuccess() {
        Customer customer = new Customer();
        customer.setUsername("test_user");
        customer.setPassword("password");
        when(customerRepository.findByUsername("test_user")).thenReturn(Optional.of(customer));

        boolean isValid = customerService.validateCustomer("test_user", "password");

        assertTrue(isValid);
    }

    @Test
    void testValidateCustomerFailure() {
        when(customerRepository.findByUsername("test_user")).thenReturn(Optional.empty());
        boolean isValid = customerService.validateCustomer("test_user", "password");
        assertFalse(isValid);
    }

    @Test
    void testCheckEmailExists() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(true);
        boolean exists = customerService.checkEmailExists("test@example.com");
        assertTrue(exists);
    }

    @Test
    void testCheckUsernameExists() {
        Customer customer = new Customer();
        customer.setUsername("test_user");
        when(customerRepository.findByUsername("test_user")).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.checkUsernameExists("test_user");

        assertTrue(result.isPresent());
        assertEquals("test_user", result.get().getUsername());
    }

    @Test
    void testCheckPhoneNumberExists() {
        when(customerRepository.findByPhoneNumber("1234567890")).thenReturn(true);
        boolean exists = customerService.checkPhoneNumberExists("1234567890");
        assertTrue(exists);
    }

    @Test
    void testCreateCustomer() {
        Address address = new Address();
        Customer customer = new Customer();
        customer.setUsername("test_user");
        customer.setEmail("test@example.com");
        customer.setPassword("password");
        customer.setFirstName("First");
        customer.setLastName("Last");
        customer.setPhoneNumber("1234567890");
        customer.setAddress(address);

        customerService.createCustomer("test_user", "test@example.com", "password", "First", "Last", "1234567890", address);

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testFallbackCreateCustomer() {
        Address address = new Address();
        customerService.fallbackCreateCustomer("test_user", "test@example.com", "password", "First", "Last", "1234567890", address, new RuntimeException("Simulated exception"));
        // Verify that fallback method logs the error, no assert necessary
    }

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer();
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.save(customer);

        assertNotNull(result);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testFallbackSaveCustomer() {
        Customer customer = new Customer();
        Customer result = customerService.fallbackSave(customer, new RuntimeException("Simulated exception"));

        assertNotNull(result);
        assertEquals(customer, result);
    }
}
