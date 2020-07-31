package com.example.myfood.Class;

public class MealCategory {
    private String url;
    private String description;

    public MealCategory(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }
}
