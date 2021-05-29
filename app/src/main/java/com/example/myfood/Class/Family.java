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

    public void addAllToFoodList(ArrayList<FoodItem> foodList) {
        if (!this.foodList.isEmpty()) {
            ArrayList<FoodItem> tmp = new ArrayList<>();
            for (FoodItem newItem : foodList) {
                boolean flag = false;
                for (FoodItem item : this.foodList) {
                    if (newItem.equals(item)) {
                        if (item.getUnit().equals(newItem.getUnit())) {
                            item.setAmount(item.getAmount() + newItem.getAmount());
                        } else {
                            item.setAmount(item.getAmount() * item.convertAmount() + newItem.getAmount() * newItem.convertAmount());
                        }
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    tmp.add(newItem);
                }
            }
            if (!tmp.isEmpty()) {
                this.foodList.addAll(tmp);
            }
        } else {
            this.shoppingList.addAll(shoppingList);

        }
    }

    public void addAllToShoppingList(ArrayList<FoodItem> shoppingList) {
        if (!this.shoppingList.isEmpty()) {
            ArrayList<FoodItem> tmp = new ArrayList<>();
            for (FoodItem newItem : shoppingList) {
                boolean flag = false;
                for (FoodItem item : this.shoppingList) {
                    if (newItem.equals(item)) {
                        if (item.getUnit().equals(newItem.getUnit())) {
                            item.setAmount(item.getAmount() + newItem.getAmount());
                        } else {
                            item.setAmount(item.getAmount() * item.convertAmount() + newItem.getAmount() * newItem.convertAmount());
                        }
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    tmp.add(newItem);
                }
            }
            if (!tmp.isEmpty()) {
                this.shoppingList.addAll(tmp);
            }
        } else {
            this.shoppingList.addAll(shoppingList);

        }
    }

    public void addToShoppingList(FoodItem newItem) {
        if (!this.shoppingList.isEmpty()) {
            boolean flag = false;
            for (FoodItem item : this.shoppingList) {
                if (newItem.equals(item)) {
                    if (item.getUnit().equals(newItem.getUnit())) {
                        item.setAmount(item.getAmount() + newItem.getAmount());
                    } else {
                        item.setAmount(item.getAmount() * item.convertAmount() + newItem.getAmount() * newItem.convertAmount());
                    }
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.shoppingList.add(newItem);
            }
        } else {
            this.shoppingList.add(newItem);

        }
    }

    public void addToFoodList(FoodItem newItem) {
        if (!this.foodList.isEmpty()) {
            boolean flag = false;
            for (FoodItem item : this.foodList) {
                if (newItem.equals(item)) {
                    if (item.getUnit().equals(newItem.getUnit())) {
                        item.setAmount(item.getAmount() + newItem.getAmount());
                    } else {
                        item.setAmount(item.getAmount() * item.convertAmount() + newItem.getAmount() * newItem.convertAmount());
                    }
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.foodList.add(newItem);
            }
        } else {
            this.foodList.add(newItem);

        }
    }
}
