package com.targetindia.EcomStreaming.service;

import com.targetindia.EcomStreaming.entites.Address;
import com.targetindia.EcomStreaming.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerAddressServiceImpl implements CustomerAddressService{

    @Autowired
    AddressRepository addressRepository;
    @Override
    public void saveCustomerAddress(Address address) {
        addressRepository.save(address);
    }
}
