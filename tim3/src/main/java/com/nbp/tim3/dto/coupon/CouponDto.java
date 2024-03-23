package com.nbp.tim3.dto.coupon;

import com.nbp.tim3.util.UUIDGenerator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class CouponDto implements Serializable {

    @Size(min = 12, max = 12, message = "Coupon code must be 12 characters long!")
    @NotNull(message = "Coupon code should not be null")
    private String      code;

    @NotNull(message = "Quantity should not be null")
    @Positive(message = "Quantity can not be negative")
    private Integer     quantity;

    @NotNull(message = "Restaurant UUID should not be null")
    private String     restaurant_uuid;

    @NotNull(message = "Discount percentage should not be null")
    @Positive(message = "Discount percentage can not be negative")
    private Integer     discount_percentage;

    //@NotNull(message="Coupon UUID must be specified!")
    @Size(min=36,max=36,message = "UUID must be 36 characters long!")
    private String      coupon_uuid;


    public CouponDto() {
        this.code = "";
        this.quantity = 0;
        this.restaurant_uuid = null;
        this.discount_percentage = 0;
        this.coupon_uuid = UUIDGenerator.generateType1UUID().toString();
    }

    public CouponDto(String code, Integer quantity, String restaurant_uuid, Integer discount_percentage) {
        this.code = code;
        this.quantity = quantity;
        this.restaurant_uuid = restaurant_uuid;
        this.discount_percentage = discount_percentage;
        this.coupon_uuid = UUIDGenerator.generateType1UUID().toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRestaurant_uuid() {
        return restaurant_uuid;
    }

    public void setRestaurant_uuid(String restaurant_uuid) {
        this.restaurant_uuid = restaurant_uuid;
    }

    public Integer getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(Integer discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getCoupon_uuid() {
        return coupon_uuid;
    }

    public void setCoupon_uuid(String coupon_uuid) {
        this.coupon_uuid = coupon_uuid;
    }
}
