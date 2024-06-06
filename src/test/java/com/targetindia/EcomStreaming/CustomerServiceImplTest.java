package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.entites.Customer;
import com.targetindia.EcomStreaming.exceptions.CustomerNotFoundException;
import com.targetindia.EcomStreaming.repository.CustomerRepository;
import com.targetindia.EcomStreaming.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceImplTest {

    @Autowired
    private CustomerServiceImpl customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void testGetCustomerByID_CustomerNotFound() {
        Long customerID = 1L;
        when(customerRepository.findById(customerID)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerByID(customerID);
        });
    }

    @Test
    public void testGetCustomerByID_CustomerFound() {
        Long customerID = 1L;
        Customer customer = new Customer();
        customer.setCustomerID(customerID);

        when(customerRepository.findById(customerID)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerByID(customerID);

        assert(result.isPresent());
        assert(result.get().getCustomerID().equals(customerID));
    }
}
