package com.thewalkingschoolbus.thewalkingschoolbus.models.collections;

public class Title {

    public static Title[] titles = new Title[]{
            new Title("the Walker", 5, false),
            new Title("the Strider", 10, false),
            new Title("the Plodder", 15, false)};

    private String title;
    private int cost;
    private boolean available;

    public Title(String title, int cost, boolean available) {
        this.title = title;
        this.cost = cost;
        this.available = available;
    }

    public String getTitle() {
        return title;
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
