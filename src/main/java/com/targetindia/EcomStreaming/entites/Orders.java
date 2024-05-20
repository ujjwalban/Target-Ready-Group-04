package com.targetindia.EcomStreaming.entites;


public class Orders {
    private Integer OrderID;
    private String CustomerName;
    private String ProductList;
    private double Price;

    public Orders(Integer orderID, String customerName, String productList, double price) {
        OrderID = orderID;
        CustomerName = customerName;
        ProductList = productList;
        Price = price;
    }

    @Override
    public String toString() {
        return "Orders [OrderID=" + OrderID + ", CustomerName=" + CustomerName + ", ProductList=" + ProductList
                + ", Price=" + Price + "]";
    }

    public Integer getOrderID() {
        return OrderID;
    }

    public void setOrderID(Integer orderID) {
        OrderID = orderID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getProductList() {
        return ProductList;
    }

    public void setProductList(String productList) {
        ProductList = productList;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
