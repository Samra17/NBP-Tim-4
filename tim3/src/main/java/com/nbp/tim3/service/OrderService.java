package com.nbp.tim3.service;

import com.nbp.tim3.dto.order.*;
import com.nbp.tim3.enums.Status;
import com.nbp.tim3.repository.OrderMenuItemRepository;
import com.nbp.tim3.repository.OrderRepository;
import com.nbp.tim3.repository.UserRepository;
import com.nbp.tim3.util.exception.InvalidRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMenuItemRepository orderMenuItemRepository;

    @Autowired
    private UserRepository userRepository;

    public OrderResponse addNewOrder(OrderCreateRequest request) {
        return orderRepository.createOrder(request);
    }

    public OrderPaginatedResponse getOrdersByCustomerId(Integer customerId, Integer page, Integer size) {
        return orderRepository.getByCustomerIdPage(customerId, page, size);
    }

    public OrderPaginatedResponse getOrdersByCourier(String username, Integer page, Integer size) {
        Integer courierId = userRepository.getByUsername(username).getId();
        return orderRepository.getByCourierIdPage(courierId, page, size);
    }

    public OrderPaginatedResponse getByRestaurantIdAndStatusPage(String managerUserananem, Status status, Integer page, Integer size) {
        return orderRepository.getByRestaurantManagerAndStatusPage(managerUserananem, status, page, size);
    }

    public Map<String, Long> getRestaurantOrdersSorted(List<String> restaurantIds, String sortType){
        return orderRepository.getRestaurantOrdersSorted(restaurantIds,sortType);
    }

    public OrderResponse getById(Integer id) {
        OrderResponse orderResponse = orderRepository.getById(id);
        if (orderResponse == null) {
            throw new EntityNotFoundException(String.format("Order with id %d does not exist!", id));
        }

        //List<String> items = orderMenuItemRepository.getOrderMenuItemsByOrder(orderResponse.getId());
        //orderResponse.setItems(items);

        return orderResponse;
    }

    public void addDeliveryPerson(Integer orderId, String username) {
        var user = userRepository.getByUsername(username);
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        orderUpdateDto.setCourierId(user.getId());
        orderUpdateDto.setOrderStatus(Status.ACCEPTED_FOR_DELIVERY);
        orderRepository.updateOrder(orderId, orderUpdateDto);
    }

    public void changeOrderStatus(Integer orderId, Status status) {
        OrderUpdateDto orderUpdateDto = new OrderUpdateDto();
        try {
            orderUpdateDto.setOrderStatus(status);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(e.getMessage());
        }
        orderRepository.updateOrder(orderId, orderUpdateDto);
    }

    public OrderPaginatedResponse getReadyForDeliveryOrders(Integer page, Integer perPage) {
        return orderRepository.getReadyForDeliveryOrdersPage(page, perPage);
    }
}
