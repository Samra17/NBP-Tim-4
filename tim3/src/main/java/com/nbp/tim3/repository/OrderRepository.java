package com.nbp.tim3.repository;

import com.nbp.tim3.dto.order.OrderCreateRequest;
import com.nbp.tim3.dto.order.OrderPaginatedResponse;
import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.dto.order.OrderUpdateDto;
import com.nbp.tim3.enums.Status;
import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class OrderRepository {

    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    private String getOrderSelectSql(String where) {
        return "SELECT ord.id as ORD_ID, ord.customer_id as CUSTOMER_ID, " +
                " ord.RESTAURANT_ID as RES_ID, ord.EST_DELIVERY_TIME as ORD_EST_DELIVERY_TIME, " +
                " ord.CREATED_AT as ORD_CREATED_AT, " +
                " ord.COUPON_ID as COUPON_ID, ord.STATUS as ORD_STATUS, ord.TOTAL_PRICE as ORD_TOTAL_PRICE, ord.COURIER_ID as COURIER_ID, " +
                " ord.DELIVERY_FEE as ORD_DELIVERY_FEE, ord.CODE as ORD_CODE, res.NAME as RES_NAME, customer.PHONE_NUMBER as CUST_PHONE_NUMBER, " +
                " address_cust.STREET AS CUST_ADD_STREET, " +
                " address_cust.MUNICIPALITY AS CUST_ADD_MUNICIPALITY," +
                " address_res.STREET AS RES_ADD_STREET, " +
                " address_res.MUNICIPALITY AS RES_ADD_MUNICIPALITY, " +
                "LISTAGG(mi.NAME || ' x ' || omi.QUANTITY, ', ') WITHIN GROUP (ORDER BY mi.ID) AS MENU_ITEMS, " +
                " COUNT(*) OVER() AS RESULT_COUNT " +
                "FROM nbp_order ord " +
                "JOIN nbp_restaurant res ON ord.restaurant_id = res.id " +
                "JOIN nbp_address address_res ON res.address_id = address_res.id " +
                "JOIN nbp.nbp_user customer ON ord.customer_id = customer.id " +
                "JOIN nbp_address address_cust ON customer.address_id = address_cust.id " +
                "LEFT JOIN nbp.nbp_user courier ON ord.courier_id = courier.id " +
                "JOIN nbp_coupon coupon ON ord.coupon_id = coupon.id " +
                "JOIN nbp_order_menu_item omi ON omi.order_id = ord.id " +
                "JOIN nbp_menu_item mi ON mi.id=omi.menu_item_id "
                + where +
                 " GROUP BY " +
                "    ord.id, ord.customer_id, ord.RESTAURANT_ID, ord.EST_DELIVERY_TIME,\n" +
                "    ord.CREATED_AT, ord.COUPON_ID, ord.STATUS, ord.TOTAL_PRICE, ord.COURIER_ID,\n" +
                "    ord.DELIVERY_FEE, ord.CODE, res.NAME, customer.PHONE_NUMBER,\n" +
                "    address_cust.STREET, address_cust.MUNICIPALITY,\n" +
                "    address_res.STREET, address_res.MUNICIPALITY";
    }

    public OrderPaginatedResponse getByRestaurantManagerAndStatusPage(String managerUseraname, Status status, Integer page, Integer size) {
        String sql =  getOrderSelectSql(
                "WHERE (UPPER(ord.status)=NVL(?, ord.status)) AND ord.restaurant_id IN " +
                "(SELECT r.id FROM nbp_restaurant r WHERE r.manager_id = " +
                "(SELECT nu.id FROM nbp.nbp_user nu WHERE nu.username=? )) "
                );

        sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        OrderPaginatedResponse orderPaginatedResponse = new OrderPaginatedResponse();
        List<OrderResponse> orders = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, status != null ? status.name() : null);
            preparedStatement.setString(2, managerUseraname);
            preparedStatement.setInt(3, (page - 1) * size);
            preparedStatement.setInt(4, size);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderResponse orderResponse = new OrderResponse();
                mapOrder(orderResponse, resultSet);
                orders.add(orderResponse);
                orderPaginatedResponse.setTotalPages((resultSet.getInt("result_count") + size - 1) / size);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        orderPaginatedResponse.setCurrentPage(page);
        orderPaginatedResponse.setOrders(orders);
        return orderPaginatedResponse;

    }


    public OrderPaginatedResponse getByCustomerIdPage(Integer customerId, Integer page, Integer size) {
        String sql = getOrderSelectSql(
                "WHERE ord.customer_id=?");
        sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return getByUserIdPage(customerId, page, size, sql);
    }

    public OrderPaginatedResponse getByCourierIdPage(Integer courierId, Integer page, Integer size) {
        String sql = getOrderSelectSql(
                "WHERE ord.courier_id=?");
        sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return getByUserIdPage(courierId, page, size, sql);
    }

    public Map<String, Long> getRestaurantOrdersSorted(List<String> restaurantIds, String sortType){
        StringBuilder sql = new StringBuilder("SELECT r.name AS restaurant_name, COUNT(o.id) AS order_count " +
                "FROM nbp_restaurant r " +
                "JOIN nbp_order o ON r.id = o.restaurant_id " +
                "WHERE r.id IN (");
        // Append the restaurant IDs to the query string
        for (int i = 0; i < restaurantIds.size(); i++) {
            sql.append("?");
            if (i < restaurantIds.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(") ");
        sql.append("GROUP BY r.name " + "ORDER BY order_count ").append(sortType.toUpperCase());

        Map<String, Long> orderMap= new HashMap<>();
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            // Set the restaurant IDs as parameters
            for (int i = 0; i < restaurantIds.size(); i++) {
                preparedStatement.setString(i + 1, restaurantIds.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                orderMap.put(resultSet.getString("restaurant_name"), (long) resultSet.getInt("order_count"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return orderMap;
    }

    private OrderPaginatedResponse getByUserIdPage(Integer userId, Integer page, Integer size, String sql) {

        OrderPaginatedResponse orderPaginatedResponse = new OrderPaginatedResponse();
        List<OrderResponse> orders = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userId);
            preparedStatement.setInt(2, (page - 1) * size);
            preparedStatement.setInt(3, size);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderResponse orderResponse = new OrderResponse();
                mapOrder(orderResponse, resultSet);
                orders.add(orderResponse);
                orderPaginatedResponse.setTotalPages((resultSet.getInt("result_count") + size-1)/size);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        orderPaginatedResponse.setCurrentPage(page);
        orderPaginatedResponse.setOrders(orders);
        return orderPaginatedResponse;
    }

    public OrderResponse getById(Integer id) {
        String sql = getOrderSelectSql(" WHERE ord.id=?");

        OrderResponse orderResponse = new OrderResponse();
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                mapOrder(orderResponse, resultSet);
                return orderResponse;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int createOrder(OrderCreateRequest orderCreateRequest) {

        String checkAddressId =
                "SELECT CASE " +
                        "           WHEN NOT EXISTS (SELECT ADDRESS_ID FROM nbp.nbp_user WHERE id = ?) THEN 1 " +
                        "           WHEN NOT EXISTS (SELECT ADDRESS_ID FROM NBP_RESTAURANT WHERE id = ?) THEN 2 " +
                        "           ELSE 3 " +
                        "       END AS result " +
                        "FROM dual";

        String sql = "INSERT INTO nbp_order (code, status, est_delivery_time," +
                " delivery_fee, total_price, coupon_id, customer_id, restaurant_id, created_at) VALUES" +
                " (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        boolean exception = false;

        Connection connection = null;
        PreparedStatement preparedStatementOrderMenuItems = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatementAddress = null;
        try {
            connection = dbConnectionService.getConnection();

            preparedStatementAddress = connection.prepareStatement(checkAddressId);
            preparedStatementAddress.setInt(1, orderCreateRequest.getCustomerId());
            preparedStatementAddress.setInt(2, orderCreateRequest.getRestaurantId());
            ResultSet resultSet = preparedStatementAddress.executeQuery();

            if (resultSet.next()) {
                int result = resultSet.getInt("result");
                if (result == 1)
                    throw new InvalidRequestException(String.format("User with id %d does not have an address!", orderCreateRequest.getCustomerId()));
                else if (result == 2)
                    throw new InvalidRequestException(String.format("Restaurant with id %d does not have an address!", orderCreateRequest.getRestaurantId()));
            } else {
                logger.error("Error checking address_id constraints.");
            }

            String[] returnCol = {"id"};
            preparedStatement = connection.prepareStatement(sql, returnCol);
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setString(2, Status.NEW.toString());
            preparedStatement.setInt(3, orderCreateRequest.getEstimatedDeliveryTime());
            preparedStatement.setFloat(4, orderCreateRequest.getDeliveryFee());
            preparedStatement.setFloat(5, orderCreateRequest.getTotalPrice());
            if (orderCreateRequest.getCouponId() != null) {
                preparedStatement.setInt(6, orderCreateRequest.getCouponId());
            } else {
                preparedStatement.setObject(6, null);
            }
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

            var menuItems = orderCreateRequest.getMenuItemIds();
            for (int i = 0; i < menuItems.size(); i++) {

                String orderMenuItemsSql = "INSERT INTO nbp_order_menu_item (quantity, menu_item_id, order_id) VALUES (?,?,?)";

                preparedStatementOrderMenuItems = connection.prepareStatement(orderMenuItemsSql);


                preparedStatementOrderMenuItems.setInt(1, menuItems.get(i).getQuantity());
                preparedStatementOrderMenuItems.setInt(2, menuItems.get(i).getId());
                preparedStatementOrderMenuItems.setInt(3, orderId);
                preparedStatementOrderMenuItems.executeUpdate();

            }


            connection.commit();
            return orderId;

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
                } else if (e.getMessage().contains("FK_ORDER_USER_CUSTOMER")) {
                    throw new InvalidRequestException(String.format("Customer with id %d already has an order!", orderCreateRequest.getCustomerId()));
                }
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            exception = true;
            logger.error(String.format("Creating new order failed: %s", e.getMessage()));
            throw new RuntimeException(e);
        } finally {
            if (exception && connection != null) {
                try {
                    if (preparedStatementOrderMenuItems != null) {
                        preparedStatementOrderMenuItems.close();
                    }
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (preparedStatementAddress != null){
                        preparedStatementAddress.close();
                    }
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

        List<String> items = List.of(resultSet.getString("MENU_ITEMS").split(", "));

        orderResponse.setItems(items);

    }

    public void updateOrder(Integer orderId, OrderUpdateDto orderUpdateDto){
        boolean exception = false;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();

            String sql = "UPDATE nbp_order " +
                    "SET status = NVL(?, status), " +
                    "courier_id = NVL(?, courier_id) " +
                    "WHERE id = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, orderUpdateDto.getOrderStatus() != null ? orderUpdateDto.getOrderStatus().name() : null);
            preparedStatement.setObject(2, orderUpdateDto.getCourierId());
            preparedStatement.setInt(3, orderId);

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
                    throw new InvalidRequestException(String.format("Courier with id %d does not exist!", orderUpdateDto.getCourierId()));
                }
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            exception = true;
            logger.error(String.format("Updating order failed: %s", e.getMessage()));
            throw new RuntimeException(e);
        } finally {
            if (exception && connection != null) {
                try {
                    Objects.requireNonNull(preparedStatement).close();;
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
