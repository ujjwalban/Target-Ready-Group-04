package com.targetindia.EcomStreaming.model;

import com.targetindia.EcomStreaming.entites.ArchivedOrders;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long productID;
    private Long productQuantity;
}

