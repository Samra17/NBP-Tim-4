package com.nbp.tim3.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRestaurantRequest {
    private String name;
    private List<Integer> categoryIds;
    private boolean isOfferingDiscount;


}
