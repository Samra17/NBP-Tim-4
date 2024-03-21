package com.nbp.tim3.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Coupon {
    int id;
    String code;
    int quantity;
    float discountPercent;

    Restaurant restaurant;
}
