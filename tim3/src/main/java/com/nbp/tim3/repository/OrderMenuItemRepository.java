package com.nbp.tim3.repository;

import com.nbp.tim3.dto.order.OrderMenuItemResponse;
import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.model.MenuItem;
import com.nbp.tim3.model.Order;
import com.nbp.tim3.model.OrderMenuItem;
import com.nbp.tim3.service.DBConnectionService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderMenuItemRepository {

    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    OrderRepository orderRepository;

    public List<OrderMenuItemResponse> getOrderMenuItemsByOrder(Integer orderId){
        String sql = "SELECT * FROM nbp_order_menu_item WHERE order_id=?";


        List<OrderMenuItemResponse> orderMenuItems = new ArrayList<>();
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, orderId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                OrderMenuItemResponse orderMenuItem = new OrderMenuItemResponse();
                orderMenuItem.setMenuItemId(resultSet.getInt("menu_item_id"));
                orderMenuItem.setOrderId(resultSet.getInt("order_id"));
                orderMenuItem.setId(resultSet.getInt("id"));
                orderMenuItem.setQuantity(resultSet.getInt("quantity"));

                orderMenuItems.add(orderMenuItem);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderMenuItems;
    }

}
