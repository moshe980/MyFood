package com.example.myfood.Class;

import java.io.Serializable;
import java.util.Objects;

public class FoodItem  implements Serializable {
    private String foodDescription;
    private String barcode;
    private double amount;
    private String available;
    private String category;
    private String unit;



    public FoodItem(String barcode,String foodDescription, double amount,String unit,String category){
        this.barcode=barcode;
        this.foodDescription =foodDescription;
        this.amount=amount;
        this.available =null;
        this.unit=unit;
        this.category=category;
    }
    public FoodItem(){}


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return Objects.equals(foodDescription, foodItem.foodDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodDescription);
    }
}
