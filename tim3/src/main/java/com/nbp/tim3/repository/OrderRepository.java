package com.nbp.tim3.repository;

import com.nbp.tim3.dto.address.AddressResponse;
import com.nbp.tim3.dto.order.OrderCreateRequest;
import com.nbp.tim3.dto.order.OrderMenuItemResponse;
import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.enums.Status;
import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepository {

    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    private final String orderSelectSql = "SELECT ord.id as ORD_ID, ord.customer_id as CUSTOMER_ID, " +
            " ord.RESTAURANT_ID as RES_ID, ord.EST_DELIVERY_TIME as ORD_EST_DELIVERY_TIME, " +
            " ord.CREATED_AT as ORD_CREATED_AT, " +
            " ord.COUPON_ID as COUPON_ID, ord.STATUS as ORD_STATUS, ord.TOTAL_PRICE as ORD_TOTAL_PRICE, ord.COURIER_ID as COURIER_ID, " +
            " ord.DELIVERY_FEE as ORD_DELIVERY_FEE, ord.CODE as ORD_CODE, res.NAME as RES_NAME, customer.PHONE_NUMBER as CUST_PHONE_NUMBER, " +
            " address_cust.STREET AS CUST_ADD_STREET, " +
            " address_cust.MUNICIPALITY AS CUST_ADD_MUNICIPALITY," +
            " address_res.STREET AS RES_ADD_STREET, " +
            " address_res.MUNICIPALITY AS RES_ADD_MUNICIPALITY " +
            "FROM nbp_order ord " +
            "JOIN nbp_restaurant res ON ord.restaurant_id = res.id " +
            "JOIN nbp_address address_res ON res.address_id = address_res.id " +
            "JOIN nbp.nbp_user customer ON ord.customer_id = customer.id " +
            "JOIN nbp_address address_cust ON customer.address_id = address_cust.id " +
            "LEFT JOIN nbp.nbp_user courier ON ord.courier_id = courier.id " +
            "JOIN nbp_coupon coupon ON ord.coupon_id = coupon.id ";

    public List<OrderResponse> getByRestaurantIdAndStatusPage(Integer restaurantId, Status status, Integer page, Integer size) {
        String sql = orderSelectSql +
                "WHERE UPPER(ord.status)=? AND ord.restaurant_id=? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<OrderResponse> orders = new ArrayList<>();
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, status.name());
            preparedStatement.setInt(2, restaurantId);
            preparedStatement.setInt(3, page * size);
            preparedStatement.setInt(4, size);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderResponse orderResponse = new OrderResponse();
                mapOrder(orderResponse, resultSet);
                orders.add(orderResponse);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;

    }


    public List<OrderResponse> getByCustomerIdPage(Integer customerId, Integer page, Integer size) {
        String sql = orderSelectSql +
                "WHERE ord.customer_id=? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return getByUserIdPage(customerId, page, size, sql);
    }

    public List<OrderResponse> getByCourierIdPage(Integer courierId, Integer page, Integer size) {
        String sql = orderSelectSql +
                "WHERE ord.courier_id=? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return getByUserIdPage(courierId, page, size, sql);
    }

    private List<OrderResponse> getByUserIdPage(Integer userId, Integer page, Integer size, String sql) {

        List<OrderResponse> orders = new ArrayList<>();
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userId);
            preparedStatement.setInt(2, page * size);
            preparedStatement.setInt(3, size);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderResponse orderResponse = new OrderResponse();
                mapOrder(orderResponse, resultSet);
                orders.add(orderResponse);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }

    public OrderResponse getById(Integer id) {
        String sql = orderSelectSql + " WHERE ord.id=?";

        OrderResponse orderResponse = new OrderResponse();
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                mapOrder(orderResponse, resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderResponse;
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

            if (e.getSQLState().startsWith("23")) {
                if (e.getMessage().contains("FK_ORDER_COUPON")) {
                    throw new InvalidRequestException(String.format("Coupon with id %d does not exist!", orderCreateRequest.getCouponId()));
                } else if (e.getMessage().contains("FK_ORDER_CUSTOMER")) {
                    throw new InvalidRequestException(String.format("Customer with id %d does not exist!", orderCreateRequest.getCustomerId()));
                } else if (e.getMessage().contains("FK_ORDER_RESTAURANT")) {
                    throw new InvalidRequestException(String.format("Restaurant with id %d does not exist!", orderCreateRequest.getRestaurantId()));
                } else if (e.getMessage().contains("FK_ORDER_MENU_ITEM_MENU_ITEM")) {
                    throw new InvalidRequestException("Menu item does not exist!");
                }
            }

        } catch (Exception e) {
            exception = true;
            logger.error(String.format("Creating new order failed: %s", e.getMessage()));
            throw e;
        } finally {
            if (exception && connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void mapOrder(OrderResponse orderResponse, ResultSet resultSet) throws SQLException {
        orderResponse.setId(resultSet.getInt("ord_id"));
        orderResponse.setOrderCode(resultSet.getString("ord_code"));
        orderResponse.setOrderStatus(Status.valueOf(resultSet.getString("ord_status").toUpperCase()));
        orderResponse.setCouponId(resultSet.getInt("coupon_id"));
        orderResponse.setCourierId(resultSet.getInt("courier_id"));
        orderResponse.setCreatedTime(new Timestamp(resultSet.getDate("ord_created_at").getTime()).toLocalDateTime());
        orderResponse.setCustomerId(resultSet.getInt("customer_id"));
        orderResponse.setCustomerAddress(resultSet.getString("cust_add_street") + ", " + resultSet.getString("cust_add_municipality"));
        orderResponse.setRestaurantAddress(resultSet.getString("res_add_street") + ", " + resultSet.getString("res_add_municipality"));

        orderResponse.setCustomerPhoneNumber(resultSet.getString("cust_phone_number"));
        orderResponse.setDeliveryFee(resultSet.getFloat("ord_delivery_fee"));
        orderResponse.setTotalPrice(resultSet.getFloat("ord_total_price"));
        orderResponse.setEstimatedDeliveryTime(resultSet.getInt("ord_est_delivery_time"));
        orderResponse.setRestaurantName(resultSet.getString("res_name"));
        orderResponse.setRestaurantId(resultSet.getInt("res_id"));

    }

    public void addDeliveryPerson(Integer orderId, Integer courierId){
        String sql = "UPDATE nbp_order SET courier_id = ? WHERE id = ?";

        boolean exception = false;

        Connection connection = null;
        try {
            connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, courierId);
            preparedStatement.setInt(2, orderId);

            int rowCount = preparedStatement.executeUpdate();

            if (rowCount < 1){
                exception = true;
                throw new InvalidRequestException(String.format("Order with id %d does not exist!", orderId));
            }

            connection.commit();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            exception = true;

            if (e.getSQLState().startsWith("23")) {
                if (e.getMessage().contains("FK_ORDER_USER_COURIER")) {
                    throw new InvalidRequestException(String.format("Courier with id %d does not exist!", courierId));
                }
            }
        } catch (Exception e) {
            exception = true;
            logger.error(String.format("Updating order failed: %s", e.getMessage()));
            throw e;
        } finally {
            if (exception && connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
