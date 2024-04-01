package com.nbp.tim3.dto.menu;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;


public class MenuCreateRequest implements Serializable {

    @NotNull(message = "Active status should not be null")
    private boolean active;

    @NotNull(message = "Name should not be null")
    @Size(min=0,max=100,message = "Name can contain a maximum of 100 characters!")
    private String name;

    @NotNull(message="Menu restaurant should not be null")
    private Integer restaurantID;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest( String name, Boolean active) {
        this.active=active;
        this.name= name;
    }

    public MenuCreateRequest(boolean active, int restaurantID, String name) {
        this.active = active;
        this.restaurantID = restaurantID;
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(Integer restaurantID) {
        this.restaurantID = restaurantID;
    }
}
