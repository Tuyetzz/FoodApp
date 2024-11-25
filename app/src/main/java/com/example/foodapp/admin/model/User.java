package com.example.foodapp.admin.model;

import java.io.Serializable;
import java.util.Map;

public class User implements Serializable {
    private String id;
    private String fullName;
    private String address;
    private String username;
    private String phone;
    private String password;
    private String role;

    public User(Map<String, Object> map) {
        this.id = (String) map.get("id");
        this.fullName = (String) map.get("fullName");
        this.address = (String) map.get("address");
        this.username = (String) map.get("username");
        this.phone = (String) map.get("phone");
        this.password = (String) map.get("password");
        this.role = (String) map.get("role");
    }

    // Constructor không tham số
    public User() {
    }

    // Constructor mới với đầy đủ các trường
    public User(String id, String fullName, String address, String username, String phone, String password, String role) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    // Constructor cũ (không có address và phone)
    public User(String fullName, String role, String password, String username, String id) {
        this.fullName = fullName;
        this.role = role;
        this.password = password;
        this.username = username;
        this.id = id;
    }

    // Getter và Setter cho các thuộc tính
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
