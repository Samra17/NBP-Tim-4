package com.nbp.tim3.repository;

import com.nbp.tim3.dto.order.OrderResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AdminInformationRepository {

    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;


    public Map<String, Long> getOrdersByRestaurant() {
        String sql =    "SELECT r.name AS restaurant_name, COUNT(o.id) AS order_count " +
                        "FROM nbp_restaurant r " +
                        "JOIN nbp_order o ON r.id = o.restaurant_id " +
                        "GROUP BY r.name";
        Map<String, Long> orderMap= new HashMap<>();
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                orderMap.put(resultSet.getString("restaurant_name"), (long) resultSet.getInt("order_count"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderMap;
    }


    public Long getTotalPriceOfOrders() {
        String sql =    "SELECT SUM(o.total_price) AS total_orders_price " +
                        "FROM nbp_order o";
        long totalSpent = 0L;
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalSpent = resultSet.getLong("total_orders_price");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalSpent;
    }

    public Map<String, Long> getRevenueByRestaurant() {
        String sql =    "SELECT r.name AS restaurant_name, SUM(o.total_price) AS total_orders_price " +
                        "FROM nbp_restaurant r " +
                        "JOIN nbp_order o ON r.id = o.restaurant_id " +
                        "GROUP BY r.name";
        Map<String, Long> orderMap= new HashMap<>();
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                orderMap.put(resultSet.getString("restaurant_name"), (long) resultSet.getInt("total_orders_price"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orderMap;
    }


}
