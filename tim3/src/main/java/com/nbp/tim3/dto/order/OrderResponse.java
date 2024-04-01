package com.nbp.tim3.dto.order;

import com.nbp.tim3.dto.address.AddressResponse;
import com.nbp.tim3.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

    private Integer id;
    private Integer customerId;
    private Integer restaurantId;
    private Integer estimatedDeliveryTime;
    private LocalDateTime createdTime;
    private Integer couponId;
    private Status orderStatus;
    private Float totalPrice;
    private Integer courierId;
    private Float deliveryFee;
    private String orderCode;
    private List<OrderMenuItemResponse> items;
    private String restaurantName;
    private String customerPhoneNumber;
    private String customerAddress;
    private String restaurantAddress;

}

