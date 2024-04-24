package com.nbp.tim3.dto.restaurantimage;

public class RestaurantImageResponse {

    int id;

    private String imageData;

    public RestaurantImageResponse(String imageData, int id) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
