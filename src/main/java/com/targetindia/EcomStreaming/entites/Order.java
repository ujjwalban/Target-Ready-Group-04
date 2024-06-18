package com.targetindia.EcomStreaming.entites;

import com.targetindia.EcomStreaming.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_product_list", joinColumns = @JoinColumn(name = "OrderID"))
    private List<Product> productList = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

//    @Temporal(TemporalType.TIMESTAMP)
//    @Transient
//    private Date expiryDate;
//
//    public void setExpiryDate(LocalDateTime localDateTime) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(this.date);
//        calendar.add(Calendar.MINUTE, 2);
//        this.expiryDate = calendar.getTime();
//    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderID=" + orderID +
                ", customer=" + customerID +
                ", products=" + productList +
                '}';
    }
}
