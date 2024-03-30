package com.nbp.tim3.service;

import com.nbp.tim3.dto.order.OrderCreateRequest;
import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.model.Order;
import com.nbp.tim3.model.OrderMenuItem;
import com.nbp.tim3.repository.OrderMenuItemRepository;
import com.nbp.tim3.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMenuItemRepository orderMenuItemRepository;

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

    public OrderResponse getById(Long id) {
        Order order = orderRepository.getById(id);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrder(order);
        List<OrderMenuItem> items = orderMenuItemRepository.getOrderMenuItemsByOrder(order);
        orderResponse.setItems(items.stream().map(OrderResponse.OrderMenuItemResponse::new).collect(Collectors.toList()));

        return orderResponse;
    }
}
