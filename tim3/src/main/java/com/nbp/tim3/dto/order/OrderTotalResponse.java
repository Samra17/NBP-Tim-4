package com.nbp.tim3.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderTotalResponse {
    private int totalCount;
    private float totalRevenue;
}
