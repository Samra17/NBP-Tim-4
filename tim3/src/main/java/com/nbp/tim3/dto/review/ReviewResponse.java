package com.nbp.tim3.dto.review;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ReviewResponse {
    private Integer id;
    private Integer userId;
    private Integer restaurantId;
    private String feedback;
    private Integer rating;
}
