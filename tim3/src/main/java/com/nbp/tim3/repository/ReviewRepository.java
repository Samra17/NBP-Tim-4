package com.nbp.tim3.repository;

import com.nbp.tim3.dto.order.OrderResponse;
import com.nbp.tim3.dto.review.ReviewCreateRequest;
import com.nbp.tim3.dto.review.ReviewPaginatedResponse;
import com.nbp.tim3.dto.review.ReviewResponse;
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
public class ReviewRepository {

    private static final Logger logger = LoggerFactory.getLogger(ReviewRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public ReviewResponse getReviewById(int id) {
        String sql = "SELECT * FROM nbp_review WHERE id=?";

        ReviewResponse reviewResponse = new ReviewResponse();
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                mapReview(reviewResponse, resultSet);
                return reviewResponse;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void mapReview(ReviewResponse reviewResponse, ResultSet resultSet) throws SQLException {
        reviewResponse.setId(resultSet.getInt("id"));
        reviewResponse.setRating(resultSet.getInt("rating"));
        reviewResponse.setFeedback(resultSet.getString("feedback"));
        reviewResponse.setRestaurantId(resultSet.getInt("restaurant_id"));
        reviewResponse.setUserId(resultSet.getInt("customer_id"));
    }

    public int createReview(ReviewCreateRequest data) {

        String sql = "INSERT INTO nbp_review (rating, feedback, restaurant_id, customer_id) " +
                "VALUES (?, ?, ?, ?)";

        boolean exception = false;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();

            String[] returnCol = {"id"};
            preparedStatement = connection.prepareStatement(sql, returnCol);
            preparedStatement.setInt(1, data.getRating());
            preparedStatement.setString(2, data.getFeedback());
            preparedStatement.setInt(3, data.getRestaurantId());
            preparedStatement.setInt(4, data.getUserId());

            int rowCount = preparedStatement.executeUpdate();

            int reviewId = 0;
            if (rowCount > 0) {
                ResultSet generatedKey = preparedStatement.getGeneratedKeys();
                if (generatedKey.next()) {
                    reviewId = generatedKey.getInt(1);
                } else {
                    logger.error("No generated id!");
                }
            }

            connection.commit();
            return reviewId;

        } catch (SQLException e) {
            logger.error(e.getMessage());
            exception = true;

            if (e.getSQLState().startsWith("23")) {
                if (e.getMessage().contains("FK_REVIEW_RESTAURANT")) {
                    throw new InvalidRequestException(String.format("Restaurant with id %d does not exist!", data.getRestaurantId()));
                } else if (e.getMessage().contains("FK_REVIEW_USER")) {
                    throw new InvalidRequestException(String.format("Customer with id %d does not exist!", data.getUserId()));
                }
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            exception = true;
            logger.error(String.format("Creating new review failed: %s", e.getMessage()));
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

    public ReviewPaginatedResponse getByRestaurantIdPage(Integer restaurantId, Integer page, Integer size) {
        String sql = "SELECT nbp_review.*, COUNT(*) OVER() result_count FROM nbp_review WHERE restaurant_id=? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return getByForeignKeyIdPage(restaurantId, page, size, sql);
    }

    public ReviewPaginatedResponse getByUserIdPage(Integer userId, Integer page, Integer size) {
        String sql = "SELECT nbp_review.*, COUNT(*) OVER() result_count FROM nbp_review WHERE customer_id=? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return getByForeignKeyIdPage(userId, page, size, sql);
    }

    private ReviewPaginatedResponse getByForeignKeyIdPage(Integer foreignKeyId, Integer page, Integer size, String sql) {

        ReviewPaginatedResponse reviewPaginatedResponse = new ReviewPaginatedResponse();
        List<ReviewResponse> reviews = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, foreignKeyId);
            preparedStatement.setInt(2, (page - 1) * size);
            preparedStatement.setInt(3, size);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ReviewResponse reviewResponse = new ReviewResponse();
                mapReview(reviewResponse, resultSet);
                reviews.add(reviewResponse);
                reviewPaginatedResponse.setTotalPages((resultSet.getInt("result_count") + size-1)/size);

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

        reviewPaginatedResponse.setCurrentPage(page);
        reviewPaginatedResponse.setReviews(reviews);
        return reviewPaginatedResponse;
    }

    public boolean deleteReview(Integer id) {

        String sql = "DELETE FROM nbp_review WHERE id=?";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            int rowCount = preparedStatement.executeUpdate();

            connection.commit();

            return rowCount > 0;

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

    public Double calculateAverageRatingForRestaurant(int restaurantId) {
        String sql = "SELECT COALESCE(AVG(rating),0.) as average FROM nbp_review WHERE restaurant_id=?";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return  resultSet.getDouble("average");
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
