package com.thewalkingschoolbus.thewalkingschoolbus.models;

/**
 * Created by Jackyx on 2018-04-04.
 */

public class CustomJson {
    private int[] avatarOwned;
    private int avatarEquipped;

    private int[] titleOwned;
    private int titleEquipped;

    private int[] themeOwned;
    private int themeEquipped;

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
