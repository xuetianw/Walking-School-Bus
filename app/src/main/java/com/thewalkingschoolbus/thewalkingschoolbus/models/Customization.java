package com.thewalkingschoolbus.thewalkingschoolbus.models;

/*
    this class is for the customJson field in User class
    which allow us to store an object as a string to the server
 */

public class Customization {
    private int[] avatarOwned;
    private int avatarEquipped;

    private int[] titleOwned;
    private int titleEquipped;

    private int[] themeOwned;
    private int themeEquipped;

    public Customization(){
        avatarOwned = null;
        avatarEquipped = -1;
        titleOwned = null;
        titleEquipped = -1;
        themeOwned = null;
        themeEquipped = -1;
    }
    public Customization(int[] avatarO, int avatarE, int[] titleO, int titleE, int[] themeO, int themeE){
        avatarOwned = avatarO;
        avatarEquipped = avatarE;
        titleOwned = titleO;
        titleEquipped = titleE;
        themeOwned = themeO;
        themeEquipped = themeE;
    }

    public int[] getAvatarOwned() {
        return avatarOwned;
    }

    public void setAvatarOwned(int[] avatarOwned) {
        this.avatarOwned = avatarOwned;
    }

    public int getAvatarEquipped() {
        return avatarEquipped;
    }

    public void setAvatarEquipped(int avatarEquipped) {
        this.avatarEquipped = avatarEquipped;
    }

    public int[] getTitleOwned() {
        return titleOwned;
    }

    public void setTitleOwned(int[] titleOwned) {
        this.titleOwned = titleOwned;
    }

    public int getTitleEquipped() {
        return titleEquipped;
    }

    public void setTitleEquipped(int titleEquipped) {
        this.titleEquipped = titleEquipped;
    }

    public int[] getThemeOwned() {
        return themeOwned;
    }

    public void setThemeOwned(int[] themeOwned) {
        this.themeOwned = themeOwned;
    }

    public int getThemeEquipped() {
        return themeEquipped;
    }

    public void setThemeEquipped(int themeEquipped) {
        this.themeEquipped = themeEquipped;
    }
}
