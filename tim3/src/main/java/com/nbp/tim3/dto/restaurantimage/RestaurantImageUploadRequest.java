package com.nbp.tim3.dto.restaurantimage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class RestaurantImageUploadRequest {
    @NotNull(message = "Image data must not be empty!")
    @NotBlank(message = "Image data must not be empty!")
    private MultipartFile imageData;

    public RestaurantImageUploadRequest( MultipartFile imageData) {
        this.imageData = imageData;
    }

    public RestaurantImageUploadRequest() {
    }


    public MultipartFile getImageData() {
        return imageData;
    }

    public void setImageData(MultipartFile imageData) {
        this.imageData = imageData;
    }
}
