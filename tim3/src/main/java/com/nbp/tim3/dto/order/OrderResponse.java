package com.nbp.tim3.dto.order;

import com.nbp.tim3.model.Order;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;

    private String userId;

    private String restaurantId;

    private String orderStatus;

    private Double totalPrice;

    private String orderCode;

    private Map<String, Long> menuItemCount;

    private String restaurantName;

    private String customerPhoneNumber;

    private String customerAddress;

    private String restaurantAddress;

    private LocalDateTime createdTime;

    private Double preparationTime;

    public OrderResponse(Order order) {
        /*this.id = order.getId();
        this.userId = order.getUser_id();
        this.restaurantId = order.getRestaurant_id();
        this.orderStatus = order.getOrderStatus();
        this.totalPrice = order.getTotalPrice();
        this.orderCode = order.getOrderCode();
        this.menuItemCount = order.getMenuItems().stream().collect(Collectors.groupingBy(MenuItem::getName, Collectors.counting()));
        this.restaurantName = order.getRestaurantName();
        this.customerPhoneNumber = order.getCustomerPhoneNumber();
        this.customerAddress = order.getCustomerAddress();
        this.createdTime = order.getCreatedTime();
        this.restaurantAddress = order.getRestaurantAddress();*/
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public Double getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Double preparationTime) {
        this.preparationTime = preparationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Map<String, Long> getMenuItemCount() {
        return menuItemCount;
    }

    public void setMenuItemCount(Map<String, Long> menuItemCount) {
        this.menuItemCount = menuItemCount;
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
