package com.example.loginapp.model;


public class Delivery {
    private String customerName;
    private String paymentType;
    private String deliveryStatus;
    private String delivered;

    public Delivery(String customerName, String paymentType, String deliveryStatus, String delivered) {
        this.customerName = customerName;
        this.paymentType = paymentType;
        this.deliveryStatus = deliveryStatus;
        this.delivered = delivered;
    }

    public Delivery (){

    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "customerName='" + customerName + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", delivered='" + delivered + '\'' +
                '}';
    }
}
