package com.nbp.tim3.repository;

import com.nbp.tim3.model.Coupon;
import com.nbp.tim3.model.Restaurant;
import com.nbp.tim3.service.DBConnectionService;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CouponRepository {

    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public Coupon getById(Integer id){
        String sql = "SELECT * FROM nbp_coupon WHERE id=?";


        Coupon coupon = new Coupon();
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                coupon.setId(resultSet.getInt("id"));
                coupon.setCode(resultSet.getString("code"));
                coupon.setQuantity(resultSet.getInt("quantity"));
                coupon.setDiscountPercent(resultSet.getFloat("discount_percent"));
                //ToDO Change after restaurant getById is implemented
                coupon.setRestaurant(new Restaurant());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return coupon;
    }

}
