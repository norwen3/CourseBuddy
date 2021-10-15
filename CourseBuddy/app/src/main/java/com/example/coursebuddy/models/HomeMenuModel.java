package com.example.coursebuddy.models;

public class HomeMenuModel {

    private String menuItemName;
    private int menuImage;
    public int destination;


    public HomeMenuModel(String menuItemName, int menuImage, int destination){
        this.menuItemName = menuItemName;
        this.menuImage = menuImage;
        this.destination = destination;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public int getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(int menuImage) {
        this.menuImage = menuImage;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }
}
