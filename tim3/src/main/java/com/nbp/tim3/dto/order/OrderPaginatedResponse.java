package com.nbp.tim3.dto.order;

import com.nbp.tim3.dto.pagination.PaginatedResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderPaginatedResponse extends PaginatedResponse {
    private List<OrderResponse> orders;
}
