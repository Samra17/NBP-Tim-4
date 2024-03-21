package com.nbp.tim3.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantImage {
    int id;
    String image;
    Restaurant restaurant;
}
