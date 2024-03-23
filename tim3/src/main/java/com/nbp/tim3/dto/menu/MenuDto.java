package com.nbp.tim3.dto.menu;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class MenuDto implements Serializable {

    private Long id;
    @NotNull(message = "Active status should not be null")
    private boolean active;

    @NotNull(message = "Name should not be null")
    private String name;

    public MenuDto() {
    }

    public MenuDto(Long id, String uuid, String name, Boolean active) {
        this.id=id;
        this.active=active;
        this.restaurant_uuid=uuid;
        this.name= name;
    }

    public MenuDto(boolean active, String restaurant_uuid, String name) {
        this.active = active;
        this.restaurant_uuid = restaurant_uuid;
        this.name = name;
    }

    @NotNull(message="Menu restaurant must be specified!")
    @Size(min=36,max=36,message = "UUID must be 36 characters long!")
    private String restaurant_uuid;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRestaurant_uuid() {
        return restaurant_uuid;
    }

    public void setRestaurant_uuid(String restaurant_uuid) {
        this.restaurant_uuid = restaurant_uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
