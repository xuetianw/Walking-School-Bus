package com.thewalkingschoolbus.thewalkingschoolbus.models.collections;

import android.content.res.Resources;

public class Avatar {

    public static Avatar[] avatars = new Avatar[]{
            new Avatar("avatar_a", 20, false),
            new Avatar("avatar_b", 25, false),
            new Avatar("avatar_c", 30, false)};

    private String name;
    private int cost;
    private boolean available;

    public Avatar(String name, int cost, boolean available) {
        this.name = name;
        this.cost = cost;
        this.available = available;
    }

    public String getName() {
        return name;
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