package com.nbp.tim3.dto.order;

import lombok.Data;

@Data
public class OrderMenuItemResponse {

    private int id;
    private int quantity;
    private int menuItemId;
    private int orderId;

}

