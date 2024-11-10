package com.example.foodapp.admin.model;

public class Order {
    private String customerName;
    private int quantity;
    private String imageUrl;
    private String orderStatus;
    private String paymentType;

    public Order(String customerName, int quantity, String imageUrl, String orderStatus, String paymentType) {
        this.customerName = customerName;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.orderStatus = orderStatus;
        this.paymentType = paymentType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerName='" + customerName + '\'' +
                ", quantity=" + quantity +
                ", imageUrl='" + imageUrl + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentType='" + paymentType + '\'' +
                '}';
    }
}
