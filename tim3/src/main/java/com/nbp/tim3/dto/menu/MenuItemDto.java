package com.nbp.tim3.dto.menu;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class MenuItemDto implements Serializable {
    @NotNull(message = "Menu item name should not be null")
    @Size(min = 2, max = 30, message = "Menu item name must be between 2 and 30 characters!")
    private String name;

    @Size(max = 100, message = "Menu item description can contain a maximum of 100 characters!")
    private String description;
    @NotNull(message = "Menu item price should not be null")
    @Min(value=0, message = "Price can not be negative")
    private Double price;

    @Min(value = 0, message = "Discount price can not be negative")
    private Double discount_price;

    @Min(value = 0, message = "Prep time can not be negative")
    private Double prep_time;

    private String image;
    public MenuItemDto() {
    }

    public MenuItemDto(String name, String description, Double price, Double discount_price, Double prep_time) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount_price = discount_price;
        this.prep_time = prep_time;
    }

    public MenuItemDto(String name, String description, Double price, Double discount_price, Double prep_time, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount_price = discount_price;
        this.prep_time = prep_time;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(Double discount_price) {
        this.discount_price = discount_price;
    }

    public Double getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(Double prep_time) {
        this.prep_time = prep_time;
    }

    @AssertTrue(message = "Discounted price should not be higher than the regular price!")
    public boolean isDiscountedPriceLessThanRegular() {
        if (discount_price != null && discount_price > price)
            return false;
        return true;
    }
}
