package com.nbp.tim3.repository;

import com.nbp.tim3.dto.pagination.PaginatedRequest;
import com.nbp.tim3.dto.restaurant.FilterRestaurantRequest;
import com.nbp.tim3.dto.restaurant.RestaurantPaginatedShortResponse;
import com.nbp.tim3.dto.restaurant.RestaurantShortResponse;
import com.nbp.tim3.model.Address;
import com.nbp.tim3.model.Restaurant;
import com.nbp.tim3.model.User;
import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class RestaurantRepository {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    @Autowired
    UserRepository userRepository;

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

    public RestaurantPaginatedShortResponse getRestaurants(PaginatedRequest request, String username, FilterRestaurantRequest filter, String sortBy, boolean ascending)  {


        Connection connection = null;

        boolean exception = false;

        String query = constructQuery(filter, sortBy, ascending);


        int paramIndex = 1;

        int offset = (request.getPage()-1)*request.getRecordsPerPage();

        User user = userRepository.getByUsername(username);

        int userId = user != null ? user.getId() : -1;

        try {
            connection = dbConnectionService.getConnection();
            String returnCols[] = { "id" };

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(paramIndex,userId);
            paramIndex +=1;

            if(filter != null) {
                if(filter.getCategoryIds() != null && !filter.getCategoryIds().isEmpty()) {
                    List<Integer> categoryIds = filter.getCategoryIds();

                    for(int i=0; i<categoryIds.size(); i++) {
                        preparedStatement.setInt(paramIndex, categoryIds.get(i));
                        paramIndex +=1;
                    }
                }

                if(filter.getName()!=null) {
                    preparedStatement.setString(paramIndex, "'%" + filter.getName() + "%'");
                    paramIndex +=1;
                }
            }


            preparedStatement.setInt(paramIndex,offset +1);
            paramIndex += 1;
            preparedStatement.setInt(paramIndex,offset + request.getRecordsPerPage());


            ResultSet resultSet = preparedStatement.executeQuery();

            RestaurantPaginatedShortResponse response = new RestaurantPaginatedShortResponse();
            response.setRestaurants(new ArrayList<>());
            response.setCurrentPage(request.getPage());

            while (resultSet.next()) {
                RestaurantShortResponse r = new RestaurantShortResponse(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("street"),
                        resultSet.getString("logo"),
                        resultSet.getInt("is_open") != 0,
                        resultSet.getString("map_coordinates"),
                        new TreeSet<>(),
                        resultSet.getFloat("rating"),
                        resultSet.getInt("customers_rated"),
                        resultSet.getInt("customers_favorited"),
                        resultSet.getInt("customer_favorite")>0);

                String categorySql = "SELECT name FROM nbp_category nc JOIN nbp_restaurant_category nrc ON " +
                        "nc.id = nrc.category_id WHERE nrc.restaurant_id=?";

                PreparedStatement preparedStatementCategories = connection.prepareStatement(categorySql);
                preparedStatementCategories.setInt(1,r.getId());

                ResultSet categoriesResultSet = preparedStatementCategories.executeQuery();

                while (categoriesResultSet.next()) {
                    r.getCategories().add(categoriesResultSet.getString("name"));
                }


                response.setTotalPages((resultSet.getInt("result_count") + request.getRecordsPerPage()-1)/request.getRecordsPerPage());
                response.getRestaurants().add(r);
            }

            connection.commit();

            return response;
        } catch (SQLException e) {
            exception = true;
            logger.error(e.getMessage());
        }
        catch (Exception e) {
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
        return null;
    }


    private String constructQuery(FilterRestaurantRequest filter,String sortBy,boolean ascending) {
        String baseQuery = "SELECT * FROM (" +
                "    SELECT COUNT(*) OVER () RESULT_COUNT,\n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET, \n" +
                "        COALESCE(AVG(nr2.rating), 0) AS rating, \n" +
                "        COUNT(DISTINCT nr2.ID) AS customers_rated, \n" +
                "        COUNT(DISTINCT nfr.ID) AS customers_favorited, \n" +
                "        CASE \n" +
                "            WHEN TO_CHAR(SYSDATE, 'HH24:MI') >= noh.opening_time \n" +
                "                 AND TO_CHAR(SYSDATE, 'HH24:MI') <= noh.closing_time THEN 1 \n" +
                "            ELSE 0 \n" +
                "        END AS is_open,\n" +
                "(SELECT COUNT(nfr2.id) FROM NBP_FAVORITE_RESTAURANT nfr2 WHERE nfr2.RESTAURANT_ID =nr.id AND nfr2.CUSTOMER_ID=? ) \n" +
                "        AS customer_favorite" +
                "    FROM \n" +
                "        NBP_RESTAURANT nr\n" +
                "    LEFT JOIN \n" +
                "        NBP_ADDRESS na ON nr.ADDRESS_ID = na.ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_REVIEW nr2 ON nr.ID = nr2.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_FAVORITE_RESTAURANT nfr ON nr.ID = nfr.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_OPENING_HOURS noh ON nr.ID = noh.RESTAURANT_ID \n" +
                "                                AND noh.DAY_OF_WEEK = (\n" +
                "                                    SELECT \n" +
                "                                        CASE TO_CHAR(SYSDATE, 'DY')\n" +
                "                                            WHEN 'MON' THEN 'Monday'\n" +
                "                                            WHEN 'TUE' THEN 'Tuesday'\n" +
                "                                            WHEN 'WED' THEN 'Wednesday'\n" +
                "                                            WHEN 'THU' THEN 'Thursday'\n" +
                "                                            WHEN 'FRI' THEN 'Friday'\n" +
                "                                            WHEN 'SAT' THEN 'Saturday'\n" +
                "                                            ELSE 'Sunday'\n" +
                "                                        END \n" +
                "                                    FROM \n" +
                "                                        dual\n" +
                "                                )";

        String optionalJoin = " JOIN NBP_RESTAURANT_CATEGORY nrc ON nr.id = nrc.restaurant_id";

        String whereClause = "";

        String groupBy = "GROUP BY \n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET, \n" +
                "        CASE \n" +
                "            WHEN TO_CHAR(SYSDATE, 'HH24:MI') >= noh.opening_time \n" +
                "                 AND TO_CHAR(SYSDATE, 'HH24:MI') <= noh.closing_time THEN 1 \n" +
                "            ELSE 0 \n" +
                "        END" ;

        String orderBy = "";

        String endQuery = ") \n" +
                "WHERE \n" +
                "    ROWNUM >= ?\n" +
                "    AND ROWNUM <= ?";

        if(filter != null) {
            if(filter.getCategoryIds() != null && !filter.getCategoryIds().isEmpty()) {

                StringJoiner joiner = new StringJoiner(",", "", "");
                for (int i = 0; i < filter.getCategoryIds().size(); i++) {
                    joiner.add("?");
                }

                String categories = "(" + joiner + ")";
                whereClause = "WHERE nrc.category_id IN " + categories;
                baseQuery += optionalJoin;
            }

            if(filter.getName() != null) {
                String whereName = "UPPER(nr.name) LIKE UPPER(?)";
                if(whereClause.isEmpty()) {
                    whereClause = "WHERE " + whereName;
                } else {
                    whereClause += " AND " + whereName;
                }
            }

            if(filter.isOfferingDiscount()) {
                String discountWhere = "EXISTS(SELECT nc.id FROM NBP_COUPON nc WHERE nc.QUANTITY>0 AND nc.RESTAURANT_ID=nr.id)";

                if(whereClause.isEmpty()) {
                    whereClause = "WHERE " + discountWhere;
                } else {
                    whereClause += " AND " + discountWhere;
                }
            }
        }

        if(sortBy!= null && !sortBy.isEmpty()) {
            String dir = "";
            if(ascending)
                dir = "ASC";
            else
                dir = "DESC";

            switch (sortBy) {
                case "POPULARITY":
                    orderBy = "ORDER BY customers_favorited " + dir;
                    break;
                case "RATING":
                    orderBy = "ORDER BY rating " + dir;
                    break;
            }
        }

        return String.format("%s %s %s %s %s",baseQuery,whereClause,groupBy,orderBy,endQuery);

    }

}
