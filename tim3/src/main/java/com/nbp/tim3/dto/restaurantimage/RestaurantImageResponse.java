package com.nbp.tim3.dto.restaurantimage;

public class RestaurantImageResponse {

    Long id;

    private String imageData;

    public RestaurantImageResponse(String imageData,Long id) {
        this.imageData = imageData;
        this.id=id;
    }

    public RestaurantImageResponse() {
    }



    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
