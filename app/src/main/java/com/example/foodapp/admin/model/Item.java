package com.example.foodapp.admin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Item implements Serializable {
    private String itemId;
    private String itemName;
    private double itemPrice;
    private String itemImage;
    private String shortDescription;
    private ArrayList<String> ingredients;

    public Item(Map<String, Object> itemMap) {
        this.itemId = (String) itemMap.get("itemId");
        this.itemName = (String) itemMap.get("itemName");
        this.itemPrice = itemMap.get("itemPrice") != null ? ((Number) itemMap.get("itemPrice")).doubleValue() : 0.0;
        this.itemImage = (String) itemMap.get("itemImage");
        this.shortDescription = (String) itemMap.get("shortDescription");

        // Chuyển đổi ingredients từ List<Object> sang ArrayList<String>
        List<Object> ingredientsRaw = (List<Object>) itemMap.get("ingredients");
        if (ingredientsRaw != null) {
            this.ingredients = new ArrayList<>();
            for (Object ingredient : ingredientsRaw) {
                this.ingredients.add(ingredient.toString());
            }
        } else {
            this.ingredients = new ArrayList<>();
        }
    }

    // Constructor
    public Item(String itemId, String itemName, double itemPrice, String itemImage, String shortDescription, ArrayList<String> ingredients) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
        this.shortDescription = shortDescription;
        this.ingredients = ingredients;
    }

    // Empty constructor (for Firebase or other serialization)
    public Item() {
    }

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemImage='" + itemImage + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
