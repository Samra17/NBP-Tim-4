package com.nbp.tim3.dto.review;

import com.nbp.tim3.dto.pagination.PaginatedResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewPaginatedResponse extends PaginatedResponse {

    private List<ReviewResponse> reviews;

}
