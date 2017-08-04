package com.example.huyng.nutrisnap;


public class FoodInfo {
    String name;
    int serving;
    int cal;
    int protein;
    int fat;

    public FoodInfo(String name, int serving, int cal, int protein, int fat) {
        this.name = name;
        this.serving = serving;
        this.cal = cal;
        this.protein = protein;
        this.fat = fat;
    }
}
