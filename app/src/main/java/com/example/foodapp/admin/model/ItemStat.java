package com.example.foodapp.admin.model;

import java.io.Serializable;

public class ItemStat implements Serializable {
    private double revenue;
    private double sellRate;

    public ItemStat(double sellRate, double revenue) {
        this.sellRate = sellRate;
        this.revenue = revenue;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getSellRate() {
        return sellRate;
    }

    public void setSellRate(double sellRate) {
        this.sellRate = sellRate;
    }

    @Override
    public String toString() {
        return "ItemStat{" +
                "revenue=" + revenue +
                ", sellRate=" + sellRate +
                '}';
    }
}
