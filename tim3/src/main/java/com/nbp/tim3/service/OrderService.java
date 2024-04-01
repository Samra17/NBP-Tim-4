package com.nbp.tim3.service;

import com.nbp.tim3.dto.order.OrderCreateRequest;
import com.nbp.tim3.dto.order.OrderMenuItemResponse;
import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.dto.order.OrderUpdateDto;
import com.nbp.tim3.enums.Status;
import com.nbp.tim3.repository.OrderMenuItemRepository;
import com.nbp.tim3.repository.OrderRepository;
import com.nbp.tim3.util.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMenuItemRepository orderMenuItemRepository;

    public void addNewOrder(OrderCreateRequest request) {
        orderRepository.createOrder(request);
    }

    public List<OrderResponse> getOrdersByCustomerId(Integer customerId, Integer page, Integer size) {
        return orderRepository.getByCustomerIdPage(customerId, page, size);
    }

    public List<OrderResponse> getOrdersByCourierId(Integer customerId, Integer page, Integer size) {
        return orderRepository.getByCourierIdPage(customerId, page, size);
    }

    public List<OrderResponse> getByRestaurantIdAndStatusPage(Integer restaurantId, Status status, Integer page, Integer size){
        return orderRepository.getByRestaurantIdAndStatusPage(restaurantId, status, page, size);
    }

    public OrderResponse getById(Integer id) {
        OrderResponse orderResponse = orderRepository.getById(id);

        List<OrderMenuItemResponse> items = orderMenuItemRepository.getOrderMenuItemsByOrder(orderResponse.getId());
        orderResponse.setItems(items);

        return orderResponse;
    }

    public void addDeliveryPerson(Integer orderId, Integer courierId) {
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setCourierId(courierId);
        orderRepository.updateOrder(orderId, orderUpdateDto);
    }

    public void changeOrderStatus(Integer orderId, String status) {
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        try {
            orderUpdateDto.setOrderStatus(Status.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(e.getMessage());
        }
        orderRepository.updateOrder(orderId, orderUpdateDto);
    }
}
