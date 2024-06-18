package com.targetindia.EcomStreaming.repository;

import com.targetindia.EcomStreaming.entites.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
}
