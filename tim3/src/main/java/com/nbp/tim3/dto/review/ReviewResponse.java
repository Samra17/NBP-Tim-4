package com.nbp.tim3.dto.review;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReviewResponse implements Serializable {
    private Long id;
    private String userUUID;
    private Long restaurantId;
    private String restaurantUUID;
    private String comment;
    private Integer rating;
    private LocalDateTime created;

    public ReviewResponse() {
    }

    public ReviewResponse(Long id, String userUUID, Long restaurantId, String restaurantUUID, String comment, Integer rating, LocalDateTime created) {
        this.id = id;
        this.userUUID = userUUID;
        this.restaurantId = restaurantId;
        this.restaurantUUID = restaurantUUID;
        this.comment = comment;
        this.rating = rating;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRestaurantUUID() {
        return restaurantUUID;
    }

    public void setRestaurantUUID(String restaurantUUID) {
        this.restaurantUUID = restaurantUUID;
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
