package com.targetindia.EcomStreaming.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerId")
    private Long customerID;

    @Column(name = "Name")
    private String name;

    @Column(name = "Pincode")
    private Long pincode;

    @Column(name = "email")
    private String email;
}
