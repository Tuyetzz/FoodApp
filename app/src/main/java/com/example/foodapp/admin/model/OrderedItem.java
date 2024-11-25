package com.example.foodapp.admin.model;

import java.io.Serializable;
import java.util.Map;

public class OrderedItem implements Serializable {
    private String id;
    private Item item;
    private int quantity;

    public OrderedItem() {

    }
    public OrderedItem(String id, Item item, int quantity) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
    }

    public OrderedItem(Map<String, Object> map) {
        this.id = (String) map.get("id");
        this.quantity = ((Long) map.get("quantity")).intValue();

        // Convert nested map to Item
        Map<String, Object> itemMap = (Map<String, Object>) map.get("item");
        if (itemMap != null) {
            this.item = new Item(itemMap);
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderedItem{" +
                "id='" + id + '\'' +
                ", item=" + item +
                ", quantity=" + quantity +
                '}';
    }
}
