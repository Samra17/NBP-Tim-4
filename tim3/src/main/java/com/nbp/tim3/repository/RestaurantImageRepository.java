package com.nbp.tim3.repository;

import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
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
import java.util.Objects;

@Repository
public class RestaurantImageRepository {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public void addImage(String imageURL, int restaurantId) {
        String sqlImage = "INSERT INTO nbp_restaurant_image(image, restaurant_id) VALUES(?,?)";

        Connection connection = null;

        boolean exception = false;

        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();

            preparedStatement = connection.prepareStatement(sqlImage);
            preparedStatement.setString(1, imageURL);
            preparedStatement.setInt(2, restaurantId);

            preparedStatement.executeQuery();

            connection.commit();

            logger.info("Successfully inserted image url into RestaurantImage.");
        } catch (SQLException e) {
            logger.error(e.getMessage());

            throw new InvalidRequestException("Error while uploading image.");
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addLogo(String imageURL, int restaurantId) {
        String sqlImage = "UPDATE nbp_restaurant SET logo=? WHERE id=?";

        Connection connection = null;

        boolean exception = false;

        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();

            preparedStatement = connection.prepareStatement(sqlImage);
            preparedStatement.setString(1, imageURL);
            preparedStatement.setInt(2, restaurantId);

            preparedStatement.executeQuery();

            connection.commit();

            logger.info("Successfully inserted image url into RestaurantImage.");
        } catch (SQLException e) {
            logger.error(e.getMessage());

            throw new InvalidRequestException("Error while uploading image.");
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteImage(int imageId) {
        String sqlImage = "DELETE FROM nbp_restaurant_image nri WHERE nri.id = ?";

        Connection connection = null;

        boolean exception = false;

        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();

            preparedStatement = connection.prepareStatement(sqlImage);
            preparedStatement.setInt(1, imageId);

            preparedStatement.executeQuery();

            connection.commit();

            logger.info("Successfully deleted image from RestaurantImage.");
        } catch (SQLException e) {
            logger.error(e.getMessage());

            throw new InvalidRequestException("Error while deleteing image.");
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<String> getImagesByRestaurantId(int restaurantId) {
        String sqlImage = "SELECT image FROM nbp_restaurant_image nri WHERE nri.restaurant_id = ?";

        Connection connection = null;

        boolean exception = false;

        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();

            preparedStatement = connection.prepareStatement(sqlImage);
            preparedStatement.setInt(1, restaurantId);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> urlList = new ArrayList<>();

            while (resultSet.next()) {
                urlList.add(resultSet.getString("image"));
            }

            connection.commit();

            logger.info("Successfully fetched images from restaurant with id " + restaurantId);

            return urlList;
        } catch (SQLException e) {
            logger.error(e.getMessage());

            throw new InvalidRequestException("Error while fetching restaurant images.");
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
