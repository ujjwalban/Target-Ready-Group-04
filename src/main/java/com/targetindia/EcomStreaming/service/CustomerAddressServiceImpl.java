package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Address;
import com.targetindia.EcomStreaming.repository.AddressRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomerAddressServiceImpl implements CustomerAddressService{

    private static final Logger logger = LoggerFactory.getLogger(CustomerAddressServiceImpl.class);

    @Autowired
    AddressRepository addressRepository;

    @Override
    @CircuitBreaker(name = "customerAddressService", fallbackMethod = "fallbackSaveCustomerAddress")
    public void saveCustomerAddress(Address address) {
        addressRepository.save(address);
    }

    public void fallbackSaveCustomerAddress(Throwable throwable){
        logger.error("Fallback method triggered due to: {}", throwable.getMessage());
    }

}
