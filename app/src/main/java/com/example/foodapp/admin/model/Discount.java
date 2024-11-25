package com.example.foodapp.admin.model;

import java.io.Serializable;

public class Discount implements Serializable {

    private String discountId;
    private String code;
    private float percentage;
    private String expiryDate;

    // Constructors
    public Discount() {
    }

    public Discount(String discountId, String code, float percentage, String expiryDate) {
        this.discountId = discountId;
        this.code = code;
        this.percentage = percentage;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }


    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "discountId='" + discountId + '\'' +
                ", code='" + code + '\'' +
                ", percentage=" + percentage +
                ", expiryDate='" + expiryDate + '\'' +
                '}';
    }
}
