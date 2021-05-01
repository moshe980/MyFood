package com.example.myfood.Class;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String email;
    private String firstName;
    private String lastName;
    private String birthDay;
    private String familyCode;
    private String num_scans;
    private String num_cooks;
    private String score;
    private static User instance;

    public static void initUser(String email, String firstName, String lastName, String birthDay,String familyCode,String num_scans,String num_cooks,String score) {
        if (instance == null) {
            instance = new User(email, firstName, lastName, birthDay,familyCode,num_scans,num_cooks,score);
        }
    }

    public User(String email, String firstName, String lastName, String birthDay,String familyCode,String num_scans,String num_cooks,String score) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.familyCode=familyCode;
        this.num_scans = num_scans;
        this.num_cooks = num_cooks;
        this.score = score;

    }

    public User() {
    }

    public static User getInstance() {
        return instance;
    }

    public String getNum_scans() {
        return num_scans;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setNum_scans(String num_scans) {
        this.num_scans = num_scans;
    }

    public String getNum_cooks() {
        return num_cooks;
    }

    public void setNum_cooks(String num_cooks) {
        this.num_cooks = num_cooks;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getBirthDay() {
        return birthDay;
    }

    public String getFamilyCode() {
        return familyCode;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(birthDay, user.birthDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName, birthDay);
    }
}
