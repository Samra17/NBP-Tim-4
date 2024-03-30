package com.nbp.tim3.repository;

import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.enums.Status;
import com.nbp.tim3.model.Coupon;
import com.nbp.tim3.model.Order;
import com.nbp.tim3.model.Restaurant;
import com.nbp.tim3.model.User;
import com.nbp.tim3.service.DBConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

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
            preparedStatement.setLong(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
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
}
