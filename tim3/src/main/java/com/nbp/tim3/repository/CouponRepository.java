package com.nbp.tim3.repository;

import com.nbp.tim3.dto.coupon.CouponCreateUpdateRequest;
import com.nbp.tim3.dto.coupon.CouponPaginatedResponse;
import com.nbp.tim3.dto.coupon.CouponResponse;
import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CouponRepository {

    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public CouponResponse getById(Integer id) {
        String sql = "SELECT * FROM nbp_coupon WHERE id=?";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                CouponResponse couponResponse = new CouponResponse();
                mapCoupon(couponResponse, resultSet);
                return couponResponse;
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

    private void mapCoupon(CouponResponse couponResponse, ResultSet resultSet) throws SQLException {
        couponResponse.setId(resultSet.getInt("id"));
        couponResponse.setDiscountPercent(resultSet.getFloat("discount_percent"));
        couponResponse.setRestaurantId(resultSet.getInt("restaurant_id"));
        couponResponse.setCode(resultSet.getString("code"));
        couponResponse.setQuantity(resultSet.getInt("quantity"));
    }

    public CouponPaginatedResponse getAll(Integer page, Integer size) {

        String sql = "SELECT nbp_coupon.*, COUNT(*) OVER() result_count FROM nbp_coupon OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        CouponPaginatedResponse couponPaginatedResponse = new CouponPaginatedResponse();
        List<CouponResponse> couponResponses = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, page);
            preparedStatement.setInt(2, size);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CouponResponse couponResponse = new CouponResponse();
                mapCoupon(couponResponse, resultSet);
                couponResponses.add(couponResponse);
                couponPaginatedResponse.setTotalPages((resultSet.getInt("result_count") + size-1)/size);
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

        couponPaginatedResponse.setCurrentPage(page);
        couponPaginatedResponse.setCouponResponse(couponResponses);
        return couponPaginatedResponse;
    }

    public int createCoupon(CouponCreateUpdateRequest data) {

        String sql = "INSERT INTO nbp_coupon (code, quantity, discount_percent, restaurant_id) " +
                "VALUES (?, ?, ?, ?)";

        boolean exception = false;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();

            String[] returnCol = {"id"};
            preparedStatement = connection.prepareStatement(sql, returnCol);
            preparedStatement.setString(1, data.getCode());
            preparedStatement.setInt(2, data.getQuantity());
            preparedStatement.setFloat(3, data.getDiscountPercent());
            preparedStatement.setInt(4, data.getRestaurantId());

            int rowCount = preparedStatement.executeUpdate();

            int couponId = 0;
            if (rowCount > 0) {
                ResultSet generatedKey = preparedStatement.getGeneratedKeys();
                if (generatedKey.next()) {
                    couponId = generatedKey.getInt(1);
                } else {
                    logger.error("No generated id!");
                }
            }

            connection.commit();
            return couponId;

        } catch (SQLException e) {
            logger.error(e.getMessage());
            exception = true;

            if (e.getSQLState().startsWith("23")) {
                if (e.getMessage().contains("FK_COUPON_RESTAURANT")) {
                    throw new InvalidRequestException(String.format("Restaurant with id %d does not exist!", data.getRestaurantId()));
                }
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            exception = true;
            logger.error(String.format("Creating new coupon failed: %s", e.getMessage()));
            throw new RuntimeException(e);
        } finally {
            if (exception && connection != null) {
                try {
                    Objects.requireNonNull(preparedStatement).close();
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public boolean updateCoupon(Integer id, Integer quantity) {

        String sql = "UPDATE nbp_coupon SET quantity = ?" +
                "WHERE id = ?";

        boolean exception = false;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, id);

            int rowCount = preparedStatement.executeUpdate();
            connection.commit();

            return rowCount > 0;

        } catch (SQLException e) {
            logger.error(e.getMessage());
            exception = true;

            throw new RuntimeException(e);
        } catch (Exception e) {
            exception = true;
            logger.error(String.format("Updating coupon failed: %s", e.getMessage()));
            throw new RuntimeException(e);
        } finally {
            if (exception && connection != null) {
                try {
                    Objects.requireNonNull(preparedStatement).close();
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public boolean deleteCoupon(Integer id) {

        String sql = "UPDATE nbp_coupon SET quantity = 0 " +
                "WHERE id = ?";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            int rowCount = preparedStatement.executeUpdate();

            connection.commit();

            return rowCount > 0;

        } catch (SQLException e) {
            logger.error(String.format("Deleting coupon failed: %s", e.getMessage()));
            throw new RuntimeException(e);
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public CouponPaginatedResponse getByRestaurantIdPage(Integer restaurantId, Integer page, Integer size) {
        String sql = "SELECT nbp_coupon.*, COUNT(*) OVER() result_count FROM nbp_coupon WHERE restaurant_id=? " +
                "ORDER BY quantity DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
        return getByForeignKeyIdPage(restaurantId, page, size, sql);
    }

    private CouponPaginatedResponse getByForeignKeyIdPage(Integer foreignKeyId, Integer page, Integer size, String sql) {

        CouponPaginatedResponse couponPaginatedResponse = new CouponPaginatedResponse();
        List<CouponResponse> coupons = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, foreignKeyId);
            preparedStatement.setInt(2, (page - 1) * size);
            preparedStatement.setInt(3, size);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CouponResponse couponResponse = new CouponResponse();
                mapCoupon(couponResponse, resultSet);
                coupons.add(couponResponse);
                couponPaginatedResponse.setTotalPages((resultSet.getInt("result_count") + size-1)/size);
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

        couponPaginatedResponse.setCurrentPage(page);
        couponPaginatedResponse.setCouponResponse(coupons);
        return couponPaginatedResponse;
    }

    public boolean applyCoupon(Integer id) {

        String checkQuantityId =
                "SELECT CASE " +
                        "           WHEN NOT EXISTS (SELECT * FROM nbp_coupon WHERE id = ? AND quantity > 0) THEN 1 " +
                        "           ELSE 2 " +
                        "       END AS result " +
                        "FROM dual";

        String sql = "UPDATE nbp_coupon SET quantity = quantity - 1 " +
                "WHERE id = ?";

        boolean exception = false;

        Connection connection = null;
        PreparedStatement preparedStatementQuantity = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();

            preparedStatementQuantity = connection.prepareStatement(checkQuantityId);
            preparedStatementQuantity.setInt(1,id);
            ResultSet resultSet = preparedStatementQuantity.executeQuery();

            if (resultSet.next()){
                int result = resultSet.getInt("result");
                if (result == 1){
                    throw new InvalidRequestException(String.format("There are no coupons with id %d left!", id));
                }
            }

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            int rowCount = preparedStatement.executeUpdate();
            connection.commit();

            return rowCount > 0;

        } catch (SQLException e) {
            logger.error(e.getMessage());
            exception = true;
            throw new RuntimeException(e);
        } catch (Exception e) {
            exception = true;
            logger.error(String.format("Updating coupon failed: %s", e.getMessage()));
            throw new RuntimeException(e);
        } finally {
            if (exception && connection != null) {
                try {
                    Objects.requireNonNull(preparedStatementQuantity).close();
                    Objects.requireNonNull(preparedStatement).close();
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public List<Integer> filterRestaurantsWithCoupons(List<Integer> restaurants) {

        if (restaurants.isEmpty()){
            throw new InvalidRequestException("List of restaurants can not be empty!");
        }

        StringBuilder sql = new StringBuilder("SELECT id " +
                "FROM nbp_restaurant " +
                "WHERE id IN (SELECT distinct(restaurant_id) FROM nbp_coupon WHERE restaurant_id IN ");

        List<Integer> filteredRestaurants = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            sql.append("(");
            sql.append("?, ".repeat(restaurants.size()-1));
            sql.append("?))");
            preparedStatement = connection.prepareStatement(sql.toString());

            for (int i=1; i<=restaurants.size(); i++){
                preparedStatement.setInt(i, restaurants.get(i-1));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                filteredRestaurants.add(resultSet.getInt("id"));
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

        return filteredRestaurants;
    }

    public CouponResponse getByCode(String code, Integer restaurantId) {
        String sql = "SELECT * FROM nbp_coupon WHERE code=? AND restaurant_id=?";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, code);
            preparedStatement.setInt(2,restaurantId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                CouponResponse couponResponse = new CouponResponse();
                mapCoupon(couponResponse, resultSet);
                return couponResponse;
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
}
