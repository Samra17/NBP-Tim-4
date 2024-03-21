package com.nbp.tim3.model;

import com.nbp.tim3.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Order {
    int id;
    String code;
    Status status;
    int estimatedDeliveryTime;
    float deliveryFee;
    float totalPrice;
    LocalDateTime createdAt;
    Coupon coupon;
    User customer;
    Restaurant restaurant;
    User courier;
}
