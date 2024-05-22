package com.targetindia.EcomStreaming.entites;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private UUID orderID;
    private Customer customer;
    private ArrayList<Product> products  = new ArrayList<>();
    private double totalCost;

    @Override
    public String toString() {
        return "Orders{" +
                "orderID=" + orderID +
                ", customer=" + customer.toString() +
                ", products=" + products.toString() +
                ", totalCost=" + totalCost +
                '}';
    }
}
