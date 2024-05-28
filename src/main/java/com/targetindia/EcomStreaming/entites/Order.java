package com.targetindia.EcomStreaming.entites;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private Long orderID;
    @Column(name = "CustomerID")
    private Long customerID;
    @Column(name = "ProductList")
    @ElementCollection
    private List<Product> productList = new ArrayList<>();
    @Override
    public String toString() {
        return "Orders{" +
                "orderID=" + orderID +
                ", customer=" + customerID +
                ", products=" + productList +
                '}';
    }
}
