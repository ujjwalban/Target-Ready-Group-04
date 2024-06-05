package com.targetindia.EcomStreaming.entites;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Products {
    @Id
    private Long ProductID;
    private String ProductName;
    private Long StockLevel;
    private double ProductPrice;

}
