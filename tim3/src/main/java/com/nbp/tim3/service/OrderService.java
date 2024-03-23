package com.nbp.tim3.service;

import com.nbp.tim3.dto.order.OrderCreateRequest;
import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.dto.restaurant.RestaurantCreateRequest;
import com.nbp.tim3.model.Order;
import com.nbp.tim3.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    //@Autowired
    //private OrderRepository reviewRepository;

    public Order addNewOrder(OrderCreateRequest request) {
        return new Order();
    }

    public List<OrderResponse> getOrdersByUserUUID(String uuid) {
        return new ArrayList<>();
    }

    public List<OrderResponse> getOrdersByDeliveryPersonId(String uuid) {
        return new ArrayList<>();
    }

    public List<OrderResponse> getPendingOrdersForRestaurant(String uuid) {
        return new ArrayList<>();
    }

    public List<OrderResponse> getInPreparationOrdersForRestaurant(String uuid) {
        return new ArrayList<>();
    }

    public List<OrderResponse> getReadyForDeliveryOrdersForRestaurant(String uuid) {
        return new ArrayList<>();
    }

    public List<OrderResponse> getDeliveredOrdersForRestaurant(String uuid) {
        return new ArrayList<>();
    }
}
