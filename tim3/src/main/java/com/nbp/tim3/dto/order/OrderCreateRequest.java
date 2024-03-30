package com.nbp.tim3.dto.order;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class OrderCreateRequest {
    private Integer restaurantId;
    private Integer estimatedDeliveryTime;
    private Integer couponId;
    private float totalPrice;
    private float deliveryFee;
    private List<OrderMenuItemPair> menuItemIds;
    private Integer customerId;

    @Getter
    @Setter
    public static class OrderMenuItemPair {
        private Integer id;
        private Integer quantity;
    }
}
