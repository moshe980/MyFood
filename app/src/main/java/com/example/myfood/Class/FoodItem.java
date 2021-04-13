package com.example.myfood.Class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class FoodItem implements Serializable {
    public final static int MILILITER = 1;
    public final static int LITER = 1000;
    public final static int GRAM = 1;
    public final static int KILOGRAM = 1000;
    public final static int CUP_OF_FLOUR = 140;
    public final static int CUP_OF_WATER = 240;
    public final static int CUP_OF_OIL = 240;
    public final static int SPOON_OF_FLOUR = 10;

    private String foodDescription;
    private String barcode;
    private double amount;
    private int available;
    private String category;
    private String unit;
    private ArrayList<String> alternativeList;


    public FoodItem(String barcode, String foodDescription, double amount, String unit, String category) {
        this.barcode = barcode;
        this.foodDescription = foodDescription;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    public ArrayList<String> getAlternativeList() {
        return alternativeList;
    }

    public void setAlternativeList(ArrayList<String> alternativeList) {
        this.alternativeList = alternativeList;
    }

    public FoodItem() {
    }


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

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
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


    public int convertAmount() {
        switch (getUnit()) {
            case "גרם":
                return GRAM;
            case "קילוגרם":
                return KILOGRAM;
            case "ליטר":
                return LITER;
            case "מ.ל":
                return MILILITER;
            case "כוסות":
                if (this.getFoodDescription().contains("קמח")) {
                    return CUP_OF_FLOUR;
                } else if (this.getFoodDescription().contains("שמן")) {
                    return CUP_OF_OIL;

                } else {
                    return CUP_OF_WATER;
                }
            case "כפות":
                return SPOON_OF_FLOUR;

            default:
                return 1;
        }
    }
}