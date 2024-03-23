package com.nbp.tim3.dto.restaurantimage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RestaurantImageUploadRequest {
    @NotNull(message = "Image data must not be empty!")
    @NotBlank(message = "Image data must not be empty!")
    private String imageData;

    public RestaurantImageUploadRequest( String imageData) {
        this.imageData = imageData;
    }

    public RestaurantImageUploadRequest() {
    }


    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
