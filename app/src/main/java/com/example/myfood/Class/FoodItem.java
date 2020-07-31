package com.example.myfood.Class;

import java.io.Serializable;
import java.util.Objects;

public class FoodItem  implements Serializable {
    private String foodDiscription;
    private int amount;
    private String unit;
    private String url;



    public FoodItem(String foodDiscription, int amount,String unit,String url){
        this.foodDiscription=foodDiscription;
        this.amount=amount;
        this.unit=unit;
        this.url=url;
    }
    public FoodItem(String foodDiscription, int amount,String unit){
        this.foodDiscription=foodDiscription;
        this.amount=amount;
        this.unit=unit;
    }

    public String getFoodDiscription() {
        return foodDiscription;
    }

    public int getAmount() {
        return amount;
    }
    public String getUnit() {
        return unit;
    }
    public String getUrl() {
        return url;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return Objects.equals(foodDiscription, foodItem.foodDiscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodDiscription);
    }
}
