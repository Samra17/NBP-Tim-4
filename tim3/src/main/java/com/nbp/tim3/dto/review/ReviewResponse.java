package com.nbp.tim3.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponse {
    private Integer id;
    private Integer userId;
    private Integer restaurantId;
    private String feedback;
    private Integer rating;
}
