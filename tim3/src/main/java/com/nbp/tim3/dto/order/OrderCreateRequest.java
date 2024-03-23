package com.nbp.tim3.dto.order;

import java.util.List;

public class OrderCreateRequest {
    private String restaurantId;

    private Integer estimatedDeliveryTime;

    private String couponId;

    private Double totalPrice;

    private Double deliveryFee;

    private List<Long> menuItemIds;

    private String restaurantName;

    private String restaurantAddress;

    private String customerPhoneNumber;

    private String customerAddress;

    public OrderCreateRequest(String restaurantId, Integer estimatedDeliveryTime, String couponId, Double totalPrice, Double deliveryFee, List<Long> menuItemIds, String restaurantName, String customerPhoneNumber, String customerAddress, String restaurantAddress) {
        this.restaurantId = restaurantId;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.couponId = couponId;
        this.totalPrice = totalPrice;
        this.deliveryFee = deliveryFee;
        this.menuItemIds = menuItemIds;
        this.restaurantName = restaurantName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerAddress = customerAddress;
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(Integer estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public List<Long> getMenuItemIds() {
        return menuItemIds;
    }

    public void setMenuItemIds(List<Long> menuItemIds) {
        this.menuItemIds = menuItemIds;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}
