package com.example.myfood.Class;

import java.io.Serializable;
import java.util.ArrayList;

public class Family implements Serializable {
    private String code;
    private String name;
    private String score;
    private ArrayList<FoodItem> foodList;
    private ArrayList<FoodItem> shoppingList;

    private static Family instance;

    public static void initFamily(String code, String name) {
        if (instance == null) {
            instance = new Family(code, name);
        }
    }

    public Family(String code, String name) {
        this.code = code;
        this.name = name;
        this.score = String.valueOf(0);
        this.foodList = new ArrayList<FoodItem>();
        this.shoppingList = new ArrayList<FoodItem>();

    }

    public static Family getInstance() {
        return instance;
    }

    public Family() {
    }

    public String getCode() {
        return code;
    }

    public ArrayList<FoodItem> getShoppingList() {
        return shoppingList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<FoodItem> getFoodList() {
        return foodList;
    }

    public String getScore() {
        return score;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
