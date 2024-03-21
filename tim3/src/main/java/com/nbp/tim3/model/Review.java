package com.nbp.tim3.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Review {
    int id;
    int rating;
    String feedback;

    User customer;

    Restaurant restaurant;
}
