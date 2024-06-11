package com.targetindia.EcomStreaming.repository;
import com.targetindia.EcomStreaming.entites.Order;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select u from Order u where u.customerID = :customerID")
    public List<Order> getHistory(@Param("customerID") Long customerID);

    @Query("SELECT o FROM Order o WHERE o.expiryDate < :currentDate")
    List<Order> findByExpiryDateBefore(@Param("currentDate") Date currentDate);
}
