package com.nbp.tim3.dto.coupon;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponCreateUpdateRequest {

    @Size(min = 3, max = 100, message = "Coupon code must be between 3 and 100 characters long!")
    @NotNull(message = "Coupon code should not be null")
    private String code;

    @NotNull(message = "Quantity should not be null")
    @Positive(message = "Quantity can not be negative")
    private Integer quantity;

    @NotNull(message = "Restaurant ID should not be null")
    private Integer restaurantId;

    @NotNull(message = "Discount percent should not be null")
    @Positive(message = "Discount percent can not be negative")
    private Integer discountPercent;
}
