package com.nbp.tim3.dto.coupon;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CouponResponse {
    private Integer id;
    private String code;
    private Integer quantity;
    private Float discountPercent;
    private Integer restaurantId;
}
