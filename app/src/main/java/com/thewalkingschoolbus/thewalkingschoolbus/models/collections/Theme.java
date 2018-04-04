package com.thewalkingschoolbus.thewalkingschoolbus.models.collections;

import android.graphics.Color;

public class Theme {

    public static Theme[] themes = new Theme[]{
            new Theme("Blue", Color.BLUE, 20, false),
            new Theme("Green", Color.GREEN, 25, false),
            new Theme("Yellow", Color.YELLOW, 30, false)};

    private String name;
    private int color;
    private int cost;
    private boolean available;

    public Theme(String name, int color, int cost, boolean available) {
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public int getCost() {
        return cost;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
