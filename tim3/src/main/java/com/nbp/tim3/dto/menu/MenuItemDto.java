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
    private Double discountPrice;

    @NotNull(message = "Prep time should not be null")
    @Min(value = 0, message = "Prep time can not be negative")
    private Integer prepTime;

    private String image;
    public MenuItemDto() {
    }

    public MenuItemDto(String name, String description, Double price, Double discountPrice, Integer prepTime) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPrice = discountPrice;
        this.prepTime = prepTime;
    }

    public MenuItemDto(String name, String description, Double price, Double discountPrice, Integer prepTime, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPrice = discountPrice;
        this.prepTime = prepTime;
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

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    @AssertTrue(message = "Discounted price should not be higher than the regular price!")
    public boolean isDiscountedPriceLessThanRegular() {
        if (discountPrice != null && discountPrice > price)
            return false;
        return true;
    }
}
