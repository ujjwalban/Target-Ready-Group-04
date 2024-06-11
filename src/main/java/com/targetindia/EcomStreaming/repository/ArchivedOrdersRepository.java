package com.targetindia.EcomStreaming.repository;

import com.targetindia.EcomStreaming.entites.ArchivedOrders;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedOrdersRepository extends JpaRepository<ArchivedOrders, Long> {
}
