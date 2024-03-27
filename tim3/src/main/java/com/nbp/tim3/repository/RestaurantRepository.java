package com.nbp.tim3.repository;

import com.nbp.tim3.model.Address;
import com.nbp.tim3.model.Category;
import com.nbp.tim3.model.Restaurant;
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

@Repository
public class RestaurantRepository {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public void addRestaurant(Restaurant restaurant, Address address, int managerId)  {
        String sqlRes = "INSERT INTO nbp_restaurant(name,address_id,manager_id) VALUES(?,?,?)";
        String sqlAdr = "INSERT INTO nbp_address(street,municipality,map_coordinates) VALUES (?,?,?)";
        String checkManagerId =
                "SELECT CASE " +
                        "           WHEN NOT EXISTS (SELECT 1 FROM nbp.nbp_user WHERE id = ?) THEN 1 " +
                        "           WHEN (SELECT name FROM nbp.nbp_role WHERE id = (SELECT role_id FROM nbp.nbp_user WHERE id = ?)) <> 'RESTAURANT_MANAGER' THEN 2 " +
                        "           WHEN EXISTS (SELECT 1 FROM nbp_restaurant WHERE manager_id = ?) THEN 3 " +
                        "           ELSE 4 " +
                        "       END AS result " +
                        "FROM dual";

        Connection connection = null;

        boolean exception = false;

        try {
            connection = dbConnectionService.getConnection();
            String returnCols[] = { "id" };

            PreparedStatement preparedStatement = connection.prepareStatement(checkManagerId);
            preparedStatement.setInt(1,managerId);
            preparedStatement.setInt(2,managerId);
            preparedStatement.setInt(3,managerId);
            ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int result = resultSet.getInt("result");
                    if(result == 1)
                        throw new InvalidRequestException(String.format("User with id %d does not exist!",managerId));
                    else if(result == 2)
                        throw new InvalidRequestException(String.format("User with id %d does not have the Manager role!",managerId));
                    else if (result == 3)
                        throw new InvalidRequestException(String.format("User with id %d already has an assigned restaurant!",managerId));
                } else {
                    logger.error("Error checking manager_id constraints.");
                }


            preparedStatement = connection.prepareStatement(sqlAdr,returnCols);
            preparedStatement.setString(1,address.getStreet());
            preparedStatement.setString(2,address.getMunicipality());
            preparedStatement.setString(3,address.getMapCoordinates());

            int rowCount = preparedStatement.executeUpdate();

            if(rowCount > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if(rs.next()) {
                    int generatedId = rs.getInt(1);
                    address.setId(generatedId);
                } else {
                    logger.error("No generated ID!");
                }
            }

            preparedStatement = connection.prepareStatement(sqlRes,returnCols);
            preparedStatement.setString(1, restaurant.getName());
            preparedStatement.setInt(2,address.getId());
            preparedStatement.setInt(3,managerId);


            rowCount = preparedStatement.executeUpdate();

            if(rowCount > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if(rs.next()) {
                    int generatedId = rs.getInt(1);
                    restaurant.setId(generatedId);
                } else {
                    logger.error("No generated ID!");
                }
            }

            connection.commit();


            logger.info(String.format("Successfully inserted %d rows into Restaurant.", rowCount));
        }

        catch (SQLException e) {
            logger.error(e.getMessage());

            exception = true;

            if(e.getSQLState().startsWith("23")) {
                if(e.getErrorCode() == 1) {
                    if (e.getMessage().contains("NBP_ADDRESS_UN"))
                        throw new InvalidRequestException(String.format("Map coordinates %s already in use!", address.getMapCoordinates()));
                    else
                        throw new InvalidRequestException(String.format("Restaurant with name %s already exists!", restaurant.getName()));
                } else if (e.getMessage().contains("FK_RESTAURANT_USER")) {
                    throw new InvalidRequestException(String.format("User with id %d does not exist!", managerId));
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
