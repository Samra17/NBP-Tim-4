package com.nbp.tim3.exception;

public class MenuItemNotFoundException extends Exception {
    public MenuItemNotFoundException() {
        super("Menu item not found!");
    }
}
