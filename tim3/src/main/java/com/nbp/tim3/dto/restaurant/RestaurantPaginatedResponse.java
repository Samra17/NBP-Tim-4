package com.nbp.tim3.dto.restaurant;

import com.nbp.tim3.dto.pagination.PaginatedResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantPaginatedResponse  extends PaginatedResponse {
    List<RestaurantResponse> restaurants;
}
