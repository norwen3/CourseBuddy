package com.example.coursebuddy.models;

import com.example.coursebuddy.iModels.IMenuModel;

public abstract class MenuModel implements IMenuModel {

    private String menuItemName;
    private int menuImage;
    private int navigation;



    public MenuModel() {

    }

    @Override
    public MenuModel setMenuModelItem(String menuItemName, int menuImage, int navigation) {
        this.menuItemName = menuItemName;
        this.menuImage = menuImage;
        this.navigation = navigation;
        return this;
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

    public int getNavigation() {
        return navigation;
    }

    public void setNavigation(int navigation) {
        this.navigation = navigation;
    }

}
