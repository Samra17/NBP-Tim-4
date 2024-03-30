package com.nbp.tim3.repository;

import com.nbp.tim3.dto.order.OrderCreateRequest;
import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.enums.Status;
import com.nbp.tim3.model.Coupon;
import com.nbp.tim3.model.Order;
import com.nbp.tim3.model.Restaurant;
import com.nbp.tim3.model.User;
import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepository {

    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;

    public Order getById(Long id) {
        String sql = "SELECT * FROM nbp_order WHERE id=?";

        Order order = new Order();
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                order.setId(resultSet.getInt("id"));
                order.setCode(resultSet.getString("code"));
                order.setStatus(Status.valueOf(resultSet.getString("status").toUpperCase()));
                order.setEstimatedDeliveryTime(resultSet.getInt("est_delivery_time"));
                order.setDeliveryFee(resultSet.getInt("delivery_fee"));
                order.setTotalPrice(resultSet.getFloat("total_price"));
                order.setCreatedAt(new Timestamp(resultSet.getDate("created_at").getTime()).toLocalDateTime());

                Coupon coupon = couponRepository.getById(resultSet.getInt("coupon_id"));
                order.setCoupon(coupon);

                User courier = userRepository.getById(resultSet.getInt("courier_id"));
                User customer = userRepository.getById(resultSet.getInt("customer_id"));

                order.setCourier(courier);
                order.setCustomer(customer);

                //ToDO Change after restaurant getById is implemented
                order.setRestaurant(new Restaurant());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;
    }

    public void createOrder(OrderCreateRequest orderCreateRequest) {
        String sql = "INSERT INTO nbp_order (code, status, est_delivery_time," +
                " delivery_fee, total_price, coupon_id, customer_id, restaurant_id, created_at) VALUES" +
                " (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        boolean exception = false;

        Connection connection = null;
        try {
            connection = dbConnectionService.getConnection();

            String[] returnCol = {"id"};
            PreparedStatement preparedStatement = connection.prepareStatement(sql, returnCol);
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setString(2, Status.NEW.toString());
            preparedStatement.setInt(3, orderCreateRequest.getEstimatedDeliveryTime());
            preparedStatement.setFloat(4, orderCreateRequest.getDeliveryFee());
            preparedStatement.setFloat(5, orderCreateRequest.getTotalPrice());
            preparedStatement.setInt(6, orderCreateRequest.getCouponId());
            preparedStatement.setInt(7, orderCreateRequest.getCustomerId());
            preparedStatement.setInt(8, orderCreateRequest.getRestaurantId());
            preparedStatement.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));

            int rowCount = preparedStatement.executeUpdate();

            int orderId = 0;
            if (rowCount > 0) {
                ResultSet generatedKey = preparedStatement.getGeneratedKeys();
                if (generatedKey.next()) {
                    orderId = generatedKey.getInt(1);
                } else {
                    logger.error("No generated id!");
                }
            }


            StringBuilder orderMenuItemsSql = new StringBuilder("INSERT INTO nbp_order_menu_item (quantity, menu_item_id, order_id) VALUES ");
            List<OrderCreateRequest.OrderMenuItemPair> menuItems = orderCreateRequest.getMenuItemIds();

            orderMenuItemsSql.append("(?, ?, ?),".repeat(menuItems.size() - 1));
            orderMenuItemsSql.append("(?, ?, ?)");
            String orderMenuItemsSqlString = orderMenuItemsSql.toString();
            PreparedStatement preparedStatementOrderMenuItems = connection.prepareStatement(orderMenuItemsSqlString);

            for (int i = 0; i < menuItems.size(); i++) {
                preparedStatementOrderMenuItems.setInt(i + 1, menuItems.get(i).getQuantity());
                preparedStatementOrderMenuItems.setInt(i + 2, menuItems.get(i).getId());
                preparedStatementOrderMenuItems.setInt(i + 3, orderId);
            }

            preparedStatementOrderMenuItems.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            exception = true;

            if(e.getSQLState().startsWith("23")) {
                if (e.getMessage().contains("FK_ORDER_COUPON")) {
                    throw new InvalidRequestException(String.format("Coupon with id %d does not exist!", orderCreateRequest.getCouponId()));
                }
                else if (e.getMessage().contains("FK_ORDER_CUSTOMER")) {
                    throw new InvalidRequestException(String.format("Customer with id %d does not exist!", orderCreateRequest.getCustomerId()));
                }
                else if (e.getMessage().contains("FK_ORDER_RESTAURANT")) {
                    throw new InvalidRequestException(String.format("Restaurant with id %d does not exist!", orderCreateRequest.getRestaurantId()));
                }
                else if (e.getMessage().contains("FK_ORDER_MENU_ITEM_MENU_ITEM")) {
                    throw new InvalidRequestException("Menu item does not exist!");
                }
            }

        } catch (Exception e) {
            exception = true;
            e.printStackTrace();
            throw e;
        } finally {
            if(exception && connection!=null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
}
