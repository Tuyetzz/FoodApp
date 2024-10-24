package com.example.loginapp.model;

public class Order {
    private String customerName;
    private int quantity;
    private String imageUrl; // URL hoặc đường dẫn đến hình ảnh

    // Constructor
    public Order(String customerName, int quantity, String imageUrl) {
        this.customerName = customerName;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Getter và Setter cho mỗi thuộc tính
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

    @Override
    public String toString() {
        return "Order{" +
                "customerName='" + customerName + '\'' +
                ", quantity=" + quantity +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
