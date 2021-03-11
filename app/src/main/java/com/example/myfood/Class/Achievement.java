package com.example.myfood.Class;

import java.io.Serializable;

public class Achievement implements Serializable {
    private String id;
    private String name;
    private String status;
    private String goal;
    private String points;

    public Achievement(String id, String name, String goal) {
        this.id = id;
        this.name = name;
        this.status = String.valueOf(0);
        this.goal = goal;
    }
    public Achievement(){};

    public String getId() {
        return id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
