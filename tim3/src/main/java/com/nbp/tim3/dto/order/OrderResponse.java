package com.nbp.tim3.dto.order;

import com.nbp.tim3.dto.address.AddressResponse;
import com.nbp.tim3.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private List<String> items;
    private String restaurantName;
    private String customerPhoneNumber;
    private String customerAddress;
    private String restaurantAddress;

}

