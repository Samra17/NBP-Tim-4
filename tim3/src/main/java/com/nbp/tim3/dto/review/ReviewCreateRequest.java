package com.nbp.tim3.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequest {

    @Size(min=2,max=100, message = "Comment must be between 2 and 100 characters!")
    private String feedback;

    @Min(value=1,message="User rating must be an integer value between 1 and 5!")
    @Max(value=5,message="User rating must be an integer value between 1 and 5!")
    private Integer rating;

    private Integer userId;

    @NotNull(message = "Restaurant ID must be specified!")
    private Integer restaurantId;
}

