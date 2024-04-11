package com.nbp.tim3.dto.coupon;

import com.nbp.tim3.dto.pagination.PaginatedResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CouponPaginatedResponse extends PaginatedResponse {
    private List<CouponResponse> couponResponse;
}
