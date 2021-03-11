package com.example.myfood.Class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeItem implements Serializable {
    private String imageUrl;
    private String name;
    private String category;
    private ArrayList<String> instructions;
    private List<Map<String, Object>> ingredients;
    private String difficultLevel;
    private String kashrot;


    public RecipeItem(String imageUrl, String name, String category, ArrayList<String> instructions, List<Map<String, Object>> ingredients, String difficultLevel, String kashrot) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.category = category;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.difficultLevel = difficultLevel;
        this.kashrot=kashrot;
    }

    public RecipeItem() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getKashrot() {
        return kashrot;
    }

    public void setKashrot(String kashrot) {
        this.kashrot = kashrot;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public List<Map<String, Object>> getIngredients() {
        return ingredients;
    }

    public String getDifficultLevel() {
        return difficultLevel;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    public void setIngredients(List<Map<String, Object>> ingredients) {
        this.ingredients = ingredients;
    }

    public void setDifficultLevel(String difficultLevel) {
        this.difficultLevel = difficultLevel;
    }

}
