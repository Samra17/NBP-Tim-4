package com.nbp.tim3.dto.restaurant;

import java.util.List;

public class FilterRestaurantRequest {
    private String name;
    private List<Long> categoryIds;
    private Boolean isOfferingDiscount;


    public FilterRestaurantRequest() {
    }

    public FilterRestaurantRequest(String name, List<Long> categoryIds, Boolean isOfferingDiscount) {
        this.name = name;
        this.categoryIds = categoryIds;
        this.isOfferingDiscount = isOfferingDiscount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Boolean getIsOfferingDiscount() {
        return isOfferingDiscount;
    }

    public void setIsOfferingDiscount(Boolean isOfferingDiscount) {
        this.isOfferingDiscount = isOfferingDiscount;
    }
}
