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

    public void updateRestaurant(Restaurant restaurant, Address address)  {
        String sqlRes = "UPDATE nbp_restaurant" +
                " SET name=?,logo=? WHERE id=?";
        String sqlUpdateAdr = "UPDATE nbp_address SET street=?, municipality=?, map_coordinates=? WHERE id=" +
                "(SELECT address_id FROM nbp_restaurant WHERE id=?)";

        Connection connection = null;

        boolean exception = false;

        try {
            connection = dbConnectionService.getConnection();
            String returnCols[] = { "id" };

          PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateAdr);
          preparedStatement.setString(1, address.getStreet());
          preparedStatement.setString(2, address.getMunicipality());
          preparedStatement.setString(3, address.getMapCoordinates());
          preparedStatement.setInt(4, restaurant.getId());

          int rowCount = preparedStatement.executeUpdate();

        if (rowCount > 0) {
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                System.out.println("ID of the updated row: " + id);
                address.setId(id);
            }
            logger.info(String.format("Updated %d rows in Address!",rowCount));
        }

        preparedStatement = connection.prepareStatement(sqlRes);
        preparedStatement.setString(1,restaurant.getName());
        preparedStatement.setString(2,restaurant.getLogo());
        preparedStatement.setInt(3,restaurant.getId());

        rowCount = preparedStatement.executeUpdate();

        connection.commit();


        logger.info(String.format("Successfully updated %d rows in Restaurant.", rowCount));


        }

        catch (SQLException e) {
            logger.error(e.getMessage());

            exception = true;

            if(e.getSQLState().startsWith("23")) {
                if(e.getErrorCode() == 1) {
                    if (e.getMessage().contains("NBP_ADDRESS_UN"))
                        throw new InvalidRequestException(String.format("Map coordinates %s already in use!", address.getMapCoordinates()));
                    else
                        throw new InvalidRequestException(String.format("Restaurant with %s name already exists!",restaurant.getName()));
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

    public boolean checkExists(int id)  {
        String sqlRes = "SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END AS exists_row " +
                "FROM nbp_restaurant WHERE id = ?";

        Connection connection = null;

        boolean exception = false;

        try {
            connection = dbConnectionService.getConnection();
            String returnCols[] = { "id" };

            PreparedStatement preparedStatement = connection.prepareStatement(sqlRes);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                int exists = resultSet.getInt(1);

                if(exists==1)
                    return true;
            }

            connection.commit();
        }

        catch (SQLException e) {
            logger.error(e.getMessage());

            exception = true;


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
        return false;
    }

}
