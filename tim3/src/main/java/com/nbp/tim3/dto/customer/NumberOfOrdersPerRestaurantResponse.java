package com.nbp.tim3.dto.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumberOfOrdersPerRestaurantResponse {
    public String restaurantName;
    public String customerName;
    public String customerAddress;
    public int numOfOrders;
}
