package com.targetindia.EcomStreaming.entites;

import com.targetindia.EcomStreaming.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "archived_order")
public class ArchivedOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private Long orderID;

    @Column(name = "CustomerID")
    private Long customerID;

    @Column(name = "ProductList")
    @ElementCollection
    @CollectionTable(name = "archived_order_product_list", joinColumns = @JoinColumn(name = "OrderID"))
    private List<Product> productList = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();
}

