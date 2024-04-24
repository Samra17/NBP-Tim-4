package com.nbp.tim3.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavoriteRestaurantResponse {
    private int id;
    private int customerId;
    private int restaurantId;
}
