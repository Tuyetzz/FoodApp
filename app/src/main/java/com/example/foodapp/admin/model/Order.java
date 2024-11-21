package com.example.foodapp.admin.model;

import java.util.Date;
import java.util.List;

public class Order {
    private String id;
    private User client;
    private User manager;
    private String orderStatus;
    private String paymentType;
    private Date orderDate; // Ngày đặt hàng
    private List<OrderedItem> listOrderedItem;

    public Order(String id, User client, User manager, String orderStatus, String paymentType, Date orderDate, List<OrderedItem> listOrderedItem) {
        this.id = id;
        this.client = client;
        this.manager = manager;
        this.orderStatus = orderStatus;
        this.paymentType = paymentType;
        this.orderDate = orderDate; // Gán giá trị ngày đặt hàng
        this.listOrderedItem = listOrderedItem;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", client=" + client +
                ", manager=" + manager +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", orderDate=" + orderDate + // Hiển thị ngày đặt hàng
                ", listOrderedItem=" + listOrderedItem +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderedItem> getListOrderedItem() {
        return listOrderedItem;
    }

    public void setListOrderedItem(List<OrderedItem> listOrderedItem) {
        this.listOrderedItem = listOrderedItem;
    }
}
