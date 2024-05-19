package com.nbp.tim3.repository;

import com.nbp.tim3.dto.order.*;
import com.nbp.tim3.enums.Status;
import com.nbp.tim3.model.Order;
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

    @Autowired
    MenuItemRepository menuItemRepository;

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
                "LEFT JOIN nbp_coupon coupon ON ord.coupon_id = coupon.id " +
                "JOIN nbp_order_menu_item omi ON omi.order_id = ord.id " +
                "JOIN nbp_menu_item mi ON mi.id=omi.menu_item_id "
                + where +
                 " GROUP BY " +
                "    ord.id, ord.customer_id, ord.RESTAURANT_ID, ord.EST_DELIVERY_TIME,\n" +
                "    ord.CREATED_AT, ord.COUPON_ID, ord.STATUS, ord.TOTAL_PRICE, ord.COURIER_ID,\n" +
                "    ord.DELIVERY_FEE, ord.CODE, res.NAME, customer.PHONE_NUMBER,\n" +
                "    address_cust.STREET, address_cust.MUNICIPALITY,\n" +
                "    address_res.STREET, address_res.MUNICIPALITY"+
                " ORDER BY ord.CREATED_AT DESC";
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
                "WHERE ord.courier_id=? AND (ord.status='IN_DELIVERY' OR ord.status='ACCEPTED_FOR_DELIVERY')");
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

    public List<OrderReportResponse> getOrderAnnualReport(Integer year) {
        String sql = "SELECT restaurant," +
                "       restaurant_name," +
                "       max(decode(Month, 1, counted, 0)) january," +
                "       max(decode(Month, 2, counted, 0)) february," +
                "       max(decode(Month, 3, counted, 0)) march," +
                "       max(decode(Month, 4, counted, 0)) april," +
                "       max(decode(Month, 5, counted, 0)) may," +
                "       max(decode(Month, 6, counted, 0)) june," +
                "       max(decode(Month, 7, counted, 0)) july," +
                "       max(decode(Month, 8, counted, 0)) august," +
                "       max(decode(Month, 9, counted, 0)) september," +
                "       max(decode(Month, 10, counted, 0)) october," +
                "       max(decode(Month, 11, counted, 0)) november," +
                "       max(decode(Month, 12, counted, 0)) december," +
                "       (SELECT COUNT(*) FROM NBP_ORDER ord WHERE ord.RESTAURANT_ID = restaurant AND extract(year from ord.CREATED_AT) = ? AND ord.STATUS = 'DELIVERED') total_count," +
                "       (" +
                "           (((SELECT COUNT(*) FROM NBP_ORDER ord WHERE ord.RESTAURANT_ID = restaurant AND extract(year from ord.CREATED_AT) = ? AND ord.STATUS = 'DELIVERED')" +
                "               -(SELECT COUNT(*) FROM NBP_ORDER ord WHERE ord.RESTAURANT_ID = restaurant AND extract(year from ord.CREATED_AT) = ? - 1 AND ord.STATUS = 'DELIVERED'))" +
                "               /NVL(NULLIF((SELECT COUNT(*) FROM NBP_ORDER ord WHERE ord.RESTAURANT_ID = restaurant AND extract(year from ord.CREATED_AT) = ? - 1 AND ord.STATUS = 'DELIVERED'), 0), 1))*100" +
                "        ) as order_increase," +
                "       max(decode(Month, 1, rev, 0)) january_rev," +
                "       max(decode(Month, 2, rev, 0)) february_rev," +
                "       max(decode(Month, 3, rev, 0)) march_rev," +
                "       max(decode(Month, 4, rev, 0)) april_rev," +
                "       max(decode(Month, 5, rev, 0)) may_rev," +
                "       max(decode(Month, 6, rev, 0)) june_rev," +
                "       max(decode(Month, 7, rev, 0)) july_rev," +
                "       max(decode(Month, 8, rev, 0)) august_rev," +
                "       max(decode(Month, 9, rev, 0)) september_rev," +
                "       max(decode(Month, 10, rev, 0)) october_rev," +
                "       max(decode(Month, 11, rev, 0)) november_rev," +
                "       max(decode(Month, 12, rev, 0)) december_rev," +
                "       (SELECT SUM(TOTAL_PRICE) FROM NBP_ORDER ord WHERE ord.RESTAURANT_ID = restaurant AND extract(year from ord.CREATED_AT) = ? AND ord.STATUS = 'DELIVERED') total_rev," +
                "       (" +
                "           (((SELECT SUM(TOTAL_PRICE) FROM NBP_ORDER ord WHERE ord.RESTAURANT_ID = restaurant AND extract(year from ord.CREATED_AT) = ? AND ord.STATUS = 'DELIVERED')" +
                "               -NVL((SELECT SUM(TOTAL_PRICE) FROM NBP_ORDER ord WHERE ord.RESTAURANT_ID = restaurant AND extract(year from ord.CREATED_AT) = ? - 1 AND ord.STATUS = 'DELIVERED'), 0))" +
                "               /NVL((SELECT SUM(TOTAL_PRICE) FROM NBP_ORDER ord WHERE ord.RESTAURANT_ID = restaurant AND extract(year from ord.CREATED_AT) = ? - 1 AND ord.STATUS = 'DELIVERED'), 1))*100" +
                "           ) as rev_increase" +
                "    FROM (" +
                "        SELECT ord.RESTAURANT_ID restaurant," +
                "               res.NAME restaurant_name" +
                "        ,extract(month from ord.CREATED_AT) Month" +
                "        ,COUNT(*) as counted" +
                "        ,SUM(TOTAL_PRICE) as rev" +
                "        FROM NBP_ORDER ord" +
                "        JOIN NBP_RESTAURANT res ON res.ID = ord.RESTAURANT_ID" +
                "        WHERE extract(year from ord.CREATED_AT) = ? AND ord.STATUS = 'DELIVERED'" +
                "        GROUP BY res.NAME, ord.RESTAURANT_ID, extract(month from ord.CREATED_AT)" +
                "    )" +
                "group by restaurant, restaurant_name";

        List<OrderReportResponse> orderReportResponses = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, year);
            preparedStatement.setInt(5, year);
            preparedStatement.setInt(6, year);
            preparedStatement.setInt(7, year);
            preparedStatement.setInt(8, year);
            preparedStatement.setInt(9, year);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderReportResponse orderReportResponse = new OrderReportResponse();
                mapOrderReportResponse(orderReportResponse, resultSet);
                orderReportResponses.add(orderReportResponse);
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

        return orderReportResponses;
    }

    public OrderTotalResponse getTotalOrderAnnualReport(Integer year) {
        String sql = "SELECT COUNT(*) as total_count, SUM(TOTAL_PRICE) as total_revenue FROM NBP_ORDER ord WHERE extract(year from ord.CREATED_AT) = ? AND ord.STATUS = 'DELIVERED'";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, year);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                OrderTotalResponse orderReportResponse = new OrderTotalResponse();
                orderReportResponse.setTotalCount(resultSet.getInt("total_count"));
                orderReportResponse.setTotalRevenue(resultSet.getFloat("total_revenue"));
                return orderReportResponse;
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

        return null;
    }

    public List<OrderCourierReportResponse> getOrderCourierAnnualReport(Integer year) {
        String sql = "SELECT courier," +
                "       courier_name," +
                "       max(decode(Month, 1, counted, 0)) january," +
                "       max(decode(Month, 2, counted, 0)) february," +
                "       max(decode(Month, 3, counted, 0)) march," +
                "       max(decode(Month, 4, counted, 0)) april," +
                "       max(decode(Month, 5, counted, 0)) may," +
                "       max(decode(Month, 6, counted, 0)) june," +
                "       max(decode(Month, 7, counted, 0)) july," +
                "       max(decode(Month, 8, counted, 0)) august," +
                "       max(decode(Month, 9, counted, 0)) september," +
                "       max(decode(Month, 10, counted, 0)) october," +
                "       max(decode(Month, 11, counted, 0)) november," +
                "       max(decode(Month, 12, counted, 0)) december," +
                "       (SELECT COUNT(*) FROM NBP_ORDER ord WHERE ord.COURIER_ID = courier AND extract(year from ord.CREATED_AT) = ?) total_count" +
                "    FROM (" +
                "        SELECT ord.COURIER_ID courier" +
                "        ,cour.first_name || ' ' || cour.last_name courier_name" +
                "        ,extract(month from ord.CREATED_AT) Month" +
                "        ,COUNT(*) as counted" +
                "        FROM NBP_ORDER ord" +
                "        JOIN NBP.NBP_USER cour ON cour.ID = ord.COURIER_ID" +
                "        WHERE extract(year from ord.CREATED_AT) = ?" +
                "        GROUP BY cour.first_name || ' ' || cour.last_name, ord.COURIER_ID, extract(month from ord.CREATED_AT)" +
                "    )" +
                "group by courier, courier_name";

        List<OrderCourierReportResponse> orderReportResponses = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, year);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderCourierReportResponse orderReportResponse = new OrderCourierReportResponse();
                mapOrderCourierReportResponse(orderReportResponse, resultSet);
                orderReportResponses.add(orderReportResponse);
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

        return orderReportResponses;
    }

    public int getTotalOrdersPerYear(Integer year) {
        String sql = "SELECT COUNT(*) total_orders FROM NBP_ORDER WHERE extract(year from CREATED_AT) = ?";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, year);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("total_orders");
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
        return 0;
    }

    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {

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

            LocalDateTime createdAt = LocalDateTime.now();
            String code = UUID.randomUUID().toString();
            String[] returnCol = {"id"};
            preparedStatement = connection.prepareStatement(sql, returnCol);
            preparedStatement.setString(1, code);
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
            preparedStatement.setTimestamp(9,Timestamp.valueOf(createdAt) );

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

            List<String> items = new ArrayList<>();
            var menuItems = orderCreateRequest.getMenuItemIds();
            for (int i = 0; i < menuItems.size(); i++) {

                String orderMenuItemsSql = "INSERT INTO nbp_order_menu_item (quantity, menu_item_id, order_id) VALUES (?,?,?)";

                preparedStatementOrderMenuItems = connection.prepareStatement(orderMenuItemsSql);

                var id = menuItems.get(i).getId();
                var quantity = menuItems.get(i).getQuantity();

                preparedStatementOrderMenuItems.setInt(1, quantity);
                preparedStatementOrderMenuItems.setInt(2, id);
                preparedStatementOrderMenuItems.setInt(3, orderId);
                preparedStatementOrderMenuItems.executeUpdate();

                var name = menuItemRepository.findById(id).getName();
                items.add(String.format("%s x %s",name, quantity));
            }


            connection.commit();
            return new OrderResponse(orderId,orderCreateRequest.getCustomerId(),orderCreateRequest.getRestaurantId(),
                    orderCreateRequest.getEstimatedDeliveryTime(),createdAt,orderCreateRequest.getCouponId(),Status.NEW,
                    orderCreateRequest.getTotalPrice(),null,orderCreateRequest.getDeliveryFee(),code,
                    items,null,null,null,null);

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
        orderResponse.setCustomerAddress(resultSet.getString("cust_add_street"));
        orderResponse.setRestaurantAddress(resultSet.getString("res_add_street"));

        orderResponse.setCustomerPhoneNumber(resultSet.getString("cust_phone_number"));
        orderResponse.setDeliveryFee(resultSet.getFloat("ord_delivery_fee"));
        orderResponse.setTotalPrice(resultSet.getFloat("ord_total_price"));
        orderResponse.setEstimatedDeliveryTime(resultSet.getInt("ord_est_delivery_time"));
        orderResponse.setRestaurantName(resultSet.getString("res_name"));
        orderResponse.setRestaurantId(resultSet.getInt("res_id"));

        List<String> items = List.of(resultSet.getString("MENU_ITEMS").split(", "));

        orderResponse.setItems(items);

    }

    private void mapOrderReportResponse(OrderReportResponse orderReportResponse, ResultSet resultSet) throws SQLException {
        orderReportResponse.setRestaurantId(resultSet.getInt("restaurant"));
        orderReportResponse.setRestaurantName(resultSet.getString("restaurant_name"));
        orderReportResponse.setJanuary(resultSet.getInt("january"));
        orderReportResponse.setFebruary(resultSet.getInt("february"));
        orderReportResponse.setMarch(resultSet.getInt("march"));
        orderReportResponse.setApril(resultSet.getInt("april"));
        orderReportResponse.setMay(resultSet.getInt("may"));
        orderReportResponse.setJune(resultSet.getInt("june"));
        orderReportResponse.setJuly(resultSet.getInt("july"));
        orderReportResponse.setAugust(resultSet.getInt("august"));
        orderReportResponse.setSeptember(resultSet.getInt("september"));
        orderReportResponse.setOctober(resultSet.getInt("october"));
        orderReportResponse.setNovember(resultSet.getInt("november"));
        orderReportResponse.setDecember(resultSet.getInt("december"));
        orderReportResponse.setTotalCount(resultSet.getInt("total_count"));
        orderReportResponse.setOrderIncrease(resultSet.getFloat("order_increase"));
        orderReportResponse.setJanuaryRev(resultSet.getFloat("january_rev"));
        orderReportResponse.setFebruaryRev(resultSet.getFloat("february_rev"));
        orderReportResponse.setMarchRev(resultSet.getFloat("march_rev"));
        orderReportResponse.setAprilRev(resultSet.getFloat("april_rev"));
        orderReportResponse.setMayRev(resultSet.getFloat("may_rev"));
        orderReportResponse.setJuneRev(resultSet.getFloat("june_rev"));
        orderReportResponse.setJulyRev(resultSet.getFloat("july_rev"));
        orderReportResponse.setAugustRev(resultSet.getFloat("august_rev"));
        orderReportResponse.setSeptemberRev(resultSet.getFloat("september_rev"));
        orderReportResponse.setOctoberRev(resultSet.getFloat("october_rev"));
        orderReportResponse.setNovemberRev(resultSet.getFloat("november_rev"));
        orderReportResponse.setDecemberRev(resultSet.getFloat("december_rev"));
        orderReportResponse.setTotalRev(resultSet.getFloat("total_rev"));
        orderReportResponse.setRevIncrease(resultSet.getFloat("rev_increase"));
    }

    private void mapOrderCourierReportResponse(OrderCourierReportResponse orderReportResponse, ResultSet resultSet) throws SQLException {
        orderReportResponse.setCourierId(resultSet.getInt("courier"));
        orderReportResponse.setCourierName(resultSet.getString("courier_name"));
        orderReportResponse.setJanuaryCour(resultSet.getInt("january"));
        orderReportResponse.setFebruaryCour(resultSet.getInt("february"));
        orderReportResponse.setMarchCour(resultSet.getInt("march"));
        orderReportResponse.setAprilCour(resultSet.getInt("april"));
        orderReportResponse.setMayCour(resultSet.getInt("may"));
        orderReportResponse.setJuneCour(resultSet.getInt("june"));
        orderReportResponse.setJulyCour(resultSet.getInt("july"));
        orderReportResponse.setAugustCour(resultSet.getInt("august"));
        orderReportResponse.setSeptemberCour(resultSet.getInt("september"));
        orderReportResponse.setOctoberCour(resultSet.getInt("october"));
        orderReportResponse.setNovemberCour(resultSet.getInt("november"));
        orderReportResponse.setDecemberCour(resultSet.getInt("december"));
        orderReportResponse.setTotalCountCour(resultSet.getInt("total_count"));
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

    public OrderPaginatedResponse getReadyForDeliveryOrdersPage(Integer page, Integer perPage) {
        String sql =  getOrderSelectSql(
                "WHERE (UPPER(ord.status)=NVL(?, ord.status)) "
        );

        sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        OrderPaginatedResponse orderPaginatedResponse = new OrderPaginatedResponse();
        List<OrderResponse> orders = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, Status.READY_FOR_DELIVERY.name());
            preparedStatement.setInt(2, (page - 1) * perPage);
            preparedStatement.setInt(3, perPage);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderResponse orderResponse = new OrderResponse();
                mapOrder(orderResponse, resultSet);
                orders.add(orderResponse);
                orderPaginatedResponse.setTotalPages((resultSet.getInt("result_count") + perPage - 1) / perPage);

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
}
