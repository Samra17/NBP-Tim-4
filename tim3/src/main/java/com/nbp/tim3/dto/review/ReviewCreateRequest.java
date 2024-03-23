package com.nbp.tim3.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReviewCreateRequest implements Serializable {
    @Size(min=2,max=100, message = "Comment must be between 2 and 100 characters!")
    private String comment;

    @Min(value=1,message="User rating must be an integer value between 1 and 5!")
    @Max(value=5,message="User rating must be an integer value between 1 and 5!")
    private Integer rating;


    private String userUUID;

    @NotNull(message = "Restaurant ID must be specified!")
    private Long restaurantId;

    public ReviewCreateRequest(String comment, Integer rating, String userUUID, Long restaurantId) {
        this.comment = comment;
        this.rating = rating;
        this.userUUID = userUUID;
        this.restaurantId = restaurantId;
    }

    public ReviewCreateRequest() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}

