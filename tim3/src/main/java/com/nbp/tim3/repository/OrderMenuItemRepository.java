package com.nbp.tim3.repository;

import com.nbp.tim3.model.MenuItem;
import com.nbp.tim3.model.Order;
import com.nbp.tim3.model.OrderMenuItem;
import com.nbp.tim3.service.DBConnectionService;
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

    public List<OrderMenuItem> getOrderMenuItemsByOrder(Order order){
        String sql = "SELECT * FROM nbp_order_menu_item WHERE order_id=?";

        List<OrderMenuItem> orderMenuItems = new ArrayList<>();
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, order.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                OrderMenuItem orderMenuItem = new OrderMenuItem();
                MenuItem menuItem = menuItemRepository.findById(resultSet.getInt("menu_item_id"));
                orderMenuItem.setMenuItem(menuItem);
                orderMenuItem.setId(resultSet.getInt("id"));
                orderMenuItem.setQuantity(resultSet.getInt("quantity"));
                orderMenuItem.setOrder(order);

                orderMenuItems.add(orderMenuItem);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderMenuItems;
    }

}
