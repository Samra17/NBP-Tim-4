package com.nbp.tim3.repository;

import com.nbp.tim3.dto.address.AddressResponse;
import com.nbp.tim3.dto.category.CategoryResponse;
import com.nbp.tim3.dto.openinghours.OpeningHoursResponse;
import com.nbp.tim3.dto.pagination.PaginatedRequest;
import com.nbp.tim3.dto.restaurant.*;
import com.nbp.tim3.model.Address;
import com.nbp.tim3.model.Category;
import com.nbp.tim3.model.Restaurant;
import com.nbp.tim3.model.User;
import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

        String query = constructQuery(filter, sortBy, ascending,-1);


        int paramIndex = 1;

        int offset = (request.getPage()-1)*request.getRecordsPerPage();

        User user = userRepository.getByUsername(username);

        int userId = user != null ? user.getId() : -1;

        try {
            connection = dbConnectionService.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(paramIndex,userId);
            paramIndex +=1;

            if(filter != null) {
                if(filter.getCategoryIds() != null && !filter.getCategoryIds().isEmpty()) {
                    List<Integer> categoryIds = filter.getCategoryIds();

                    for (Integer categoryId : categoryIds) {
                        preparedStatement.setInt(paramIndex, categoryId);
                        paramIndex += 1;
                    }
                }

                if(filter.getName()!=null) {
                    preparedStatement.setString(paramIndex, "%" + filter.getName() + "%");
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
                        resultSet.getString("categories") != null ?
                        new TreeSet<>(Arrays.asList(resultSet.getString("categories").split(",")))
                        : new TreeSet<String>(),
                        resultSet.getFloat("rating"),
                        resultSet.getInt("customers_rated"),
                        resultSet.getInt("customers_favorited"),
                        resultSet.getInt("customer_favorite")>0);


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

    public RestaurantShortResponse getRestaurantShortResponseById(int id, String username) {
        Connection connection = null;

        boolean exception = false;

        String query = constructQuery(null, null, false,id);



        int paramIndex = 1;

        User user = userRepository.getByUsername(username);

        int userId = user != null ? user.getId() : -1;

        try {
            connection = dbConnectionService.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(paramIndex,userId);
            paramIndex +=1;

            preparedStatement.setInt(paramIndex,id);
            paramIndex +=1;

            preparedStatement.setInt(paramIndex,1);
            paramIndex +=1;
            preparedStatement.setInt(paramIndex,1);


            ResultSet resultSet = preparedStatement.executeQuery();

            RestaurantShortResponse response = null;

            if (resultSet.next()) {
                 response = new RestaurantShortResponse(id, resultSet.getString("name"),
                        resultSet.getString("street"),
                        resultSet.getString("logo"),
                        resultSet.getInt("is_open") != 0,
                        resultSet.getString("map_coordinates"),
                         resultSet.getString("categories") != null ?
                                 new TreeSet<>(Arrays.asList(resultSet.getString("categories").split(",")))
                                 : new TreeSet<String>(),
                        resultSet.getFloat("rating"),
                        resultSet.getInt("customers_rated"),
                        resultSet.getInt("customers_favorited"),
                        resultSet.getInt("customer_favorite")>0);



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


    public RestaurantPaginatedResponse getFullRestaurants(PaginatedRequest paginatedRequest) {
        Connection connection = null;

        boolean exception = false;

        String query = "SELECT * FROM (    SELECT ROW_NUMBER() OVER (ORDER BY nr.id) rnum," +
                " COUNT(DISTINCT nr.id) OVER () RESULT_COUNT,\n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        nr.ADDRESS_ID,\n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET, \n" +
                "        na.MUNICIPALITY ,\n" +
                "        nr.MANAGER_ID,\n" +
                "        LISTAGG(DISTINCT  nc.id ||' ' || nc.name, ',') WITHIN GROUP (ORDER BY nc.NAME) AS categories,\n" +
                "        LISTAGG(DISTINCT  noh.DAY_OF_WEEK  ||' ' || noh.OPENING_TIME || '-' || noh.CLOSING_TIME , ',') WITHIN GROUP (ORDER BY noh.DAY_OF_WEEK) AS opening_times,\n" +
                "        COALESCE(AVG(nr2.rating), 0) AS rating, \n" +
                "        COUNT(DISTINCT nr2.ID) AS customers_rated, \n" +
                "        COUNT(DISTINCT nfr.ID) AS customers_favorited\n" +
                "        \n" +
                "     FROM \n" +
                "        NBP_RESTAURANT nr\n" +
                "    LEFT JOIN \n" +
                "        NBP_ADDRESS na ON nr.ADDRESS_ID = na.ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_REVIEW nr2 ON nr.ID = nr2.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_FAVORITE_RESTAURANT nfr ON nr.ID = nfr.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_OPENING_HOURS noh ON nr.ID = noh.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "    \tNBP_RESTAURANT_CATEGORY nrc ON nrc.RESTAURANT_ID = nr.ID \n" +
                "    LEFT JOIN \n" +
                "    \t NBP_CATEGORY nc ON nc.ID = nrc.CATEGORY_ID \n" +
                "\n" +
                "GROUP BY \n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        nr.ADDRESS_ID,\n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET,\n" +
                "        na.MUNICIPALITY,\n" +
                "        nr.MANAGER_ID\n" +
                "        \n" +
                "ORDER BY nr.id\n" +
                "        )\n" +
                "\n" +
                "WHERE \n" +
                "    rnum >= ?\n" +
                "    AND rnum <= ?";


        int offset = (paginatedRequest.getPage()-1)*paginatedRequest.getRecordsPerPage();


        try {
            connection = dbConnectionService.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);


            preparedStatement.setInt(1,offset +1);
            preparedStatement.setInt(2,offset + paginatedRequest.getRecordsPerPage());


            ResultSet resultSet = preparedStatement.executeQuery();

            RestaurantPaginatedResponse response = new RestaurantPaginatedResponse();
            response.setRestaurants(new ArrayList<>());
            response.setCurrentPage(paginatedRequest.getPage());

            while (resultSet.next()) {
                RestaurantResponse r = new RestaurantResponse(resultSet.getInt("id"), resultSet.getString("name"),
                        new AddressResponse(resultSet.getInt("address_id"),resultSet.getString("street"),
                                resultSet.getString("municipality"), resultSet.getString("map_coordinates")),
                        resultSet.getString("logo"),
                        resultSet.getInt("manager_id") ,
                        null,
                        null,
                        resultSet.getFloat("rating"),
                        resultSet.getInt("customers_rated"),
                        resultSet.getInt("customers_favorited"));

                List<String> categories = resultSet.getString("categories") != null ?
                        Arrays.asList(resultSet.getString("categories").split(",")) : new ArrayList<>();

                if(!categories.isEmpty()) {
                    List<CategoryResponse> categoryResponses = new ArrayList<>();

                    categories.forEach(c -> {
                        Pattern pattern = Pattern.compile("(\\d+)\\s(.+)");
                        Matcher matcher = pattern.matcher(c);
                                if (matcher.find()) {
                                    int id = Integer.parseInt(matcher.group(1));
                                    String name = matcher.group(2);
                                    CategoryResponse categoryResponse= new CategoryResponse(id,name);
                                    categoryResponses.add(categoryResponse);
                                }
                    });
                    r.setCategories(categoryResponses);
                }

                List<String> openingTimes = resultSet.getString("opening_times") != null ?
                        Arrays.asList(resultSet.getString("opening_times").split(",")) : new ArrayList<>();

                if(!openingTimes.isEmpty()) {
                    OpeningHoursResponse openingHoursResponse = new OpeningHoursResponse();
                    openingTimes.forEach(ot -> {
                        Pattern pattern = Pattern.compile("([a-zA-Z]+)\\s(\\d{2}:\\d{2})-(\\d{2}:\\d{2})");
                        Matcher matcher = pattern.matcher(ot);
                        if (matcher.find()) {
                            String day = matcher.group(1);
                            String open = matcher.group(2);
                            String close = matcher.group(3);

                            switch (day){
                                case "Monday":
                                    openingHoursResponse.setMondayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setMondayClose(LocalTime.parse(close));
                                    break;
                                case "Tuesday":
                                    openingHoursResponse.setTuesdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setTuesdayClose(LocalTime.parse(close));
                                    break;
                                case "Wednesday":
                                    openingHoursResponse.setWednesdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setWednesdayClose(LocalTime.parse(close));
                                    break;
                                case "Thursday":
                                    openingHoursResponse.setThursdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setThursdayClose(LocalTime.parse(close));
                                    break;
                                case "Friday":
                                    openingHoursResponse.setFridayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setFridayClose(LocalTime.parse(close));
                                    break;
                                case "Saturday":
                                    openingHoursResponse.setSaturdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setSaturdayClose(LocalTime.parse(close));
                                    break;
                                case "Sunday":
                                    openingHoursResponse.setSundayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setSundayClose(LocalTime.parse(close));
                                    break;
                            }
                        }
                    });

                    r.setOpeningHours(openingHoursResponse);
                }


                response.setTotalPages((resultSet.getInt("result_count") + paginatedRequest.getRecordsPerPage()-1)/paginatedRequest.getRecordsPerPage());
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

    public Integer getRestaurantIdByManagerUsername(String managerUsername) {
        Connection connection = null;

        boolean exception = false;

        String query = "SELECT nr.id " +
                "FROM nbp_restaurant nr " +
                "LEFT JOIN nbp.nbp_user nu ON nu.id=nr.manager_id " +
                "WHERE nu.username =?";


        try {
            connection = dbConnectionService.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1,managerUsername);

            ResultSet resultSet = preparedStatement.executeQuery();

            Integer response = null;

            if (resultSet.next()) {
               int id = resultSet.getInt("id");
               response = Integer.valueOf(id);
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

    public RestaurantResponse getRestaurantByManagerUsername(String managerUsername) {
        Connection connection = null;

        boolean exception = false;

        String query = "SELECT \n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        nr.ADDRESS_ID,\n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET, \n" +
                "        na.MUNICIPALITY ,\n" +
                "        nr.MANAGER_ID,\n" +
                "        LISTAGG(DISTINCT  nc.id ||' ' || nc.name, ',') WITHIN GROUP (ORDER BY nc.NAME) AS categories,\n" +
                "        LISTAGG(DISTINCT  noh.DAY_OF_WEEK  ||' ' || noh.OPENING_TIME || '-' || noh.CLOSING_TIME , ',') WITHIN GROUP (ORDER BY noh.DAY_OF_WEEK) AS opening_times,\n" +
                "        COALESCE(AVG(nr2.rating), 0) AS rating, \n" +
                "        COUNT(DISTINCT nr2.ID) AS customers_rated, \n" +
                "        COUNT(DISTINCT nfr.ID) AS customers_favorited\n" +
                "        \n" +
                "     FROM \n" +
                "        NBP_RESTAURANT nr\n" +
                "    LEFT JOIN \n" +
                "        NBP_ADDRESS na ON nr.ADDRESS_ID = na.ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_REVIEW nr2 ON nr.ID = nr2.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_FAVORITE_RESTAURANT nfr ON nr.ID = nfr.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_OPENING_HOURS noh ON nr.ID = noh.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "    \tNBP_RESTAURANT_CATEGORY nrc ON nrc.RESTAURANT_ID = nr.ID \n" +
                "    LEFT JOIN \n" +
                "    \t NBP_CATEGORY nc ON nc.ID = nrc.CATEGORY_ID \n" +
                " LEFT JOIN nbp.nbp_user nu ON nu.id=nr.manager_id " +
                "\n WHERE nu.username = ?" +
                "GROUP BY \n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        nr.ADDRESS_ID,\n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET,\n" +
                "        na.MUNICIPALITY,\n" +
                "        nr.MANAGER_ID";




        try {
            connection = dbConnectionService.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);


            preparedStatement.setString(1,managerUsername);

            ResultSet resultSet = preparedStatement.executeQuery();

            RestaurantResponse response = null;


            if (resultSet.next()) {
                response = new RestaurantResponse(resultSet.getInt("id"), resultSet.getString("name"),
                        new AddressResponse(resultSet.getInt("address_id"),resultSet.getString("street"),
                                resultSet.getString("municipality"), resultSet.getString("map_coordinates")),
                        resultSet.getString("logo"),
                        resultSet.getInt("manager_id") ,
                        null,
                        null,
                        resultSet.getFloat("rating"),
                        resultSet.getInt("customers_rated"),
                        resultSet.getInt("customers_favorited"));

                List<String> categories = resultSet.getString("categories") != null ?
                        Arrays.asList(resultSet.getString("categories").split(",")) : new ArrayList<>();

                if(!categories.isEmpty()) {
                    List<CategoryResponse> categoryResponses = new ArrayList<>();

                    categories.forEach(c -> {
                        Pattern pattern = Pattern.compile("(\\d+)\\s(.+)");
                        Matcher matcher = pattern.matcher(c);
                        if (matcher.find()) {
                            int id = Integer.parseInt(matcher.group(1));
                            String name = matcher.group(2);
                            CategoryResponse categoryResponse= new CategoryResponse(id,name);
                            categoryResponses.add(categoryResponse);
                        }
                    });
                    response.setCategories(categoryResponses);
                }

                List<String> openingTimes = resultSet.getString("opening_times") != null ?
                        Arrays.asList(resultSet.getString("opening_times").split(",")) : new ArrayList<>();

                if(!openingTimes.isEmpty()) {
                    OpeningHoursResponse openingHoursResponse = new OpeningHoursResponse();
                    openingTimes.forEach(ot -> {
                        Pattern pattern = Pattern.compile("([a-zA-Z]+)\\s(\\d{2}:\\d{2})-(\\d{2}:\\d{2})");
                        Matcher matcher = pattern.matcher(ot);
                        if (matcher.find()) {
                            String day = matcher.group(1);
                            String open = matcher.group(2);
                            String close = matcher.group(3);

                            switch (day){
                                case "Monday":
                                    openingHoursResponse.setMondayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setMondayClose(LocalTime.parse(close));
                                    break;
                                case "Tuesday":
                                    openingHoursResponse.setTuesdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setTuesdayClose(LocalTime.parse(close));
                                    break;
                                case "Wednesday":
                                    openingHoursResponse.setWednesdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setWednesdayClose(LocalTime.parse(close));
                                    break;
                                case "Thursday":
                                    openingHoursResponse.setThursdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setThursdayClose(LocalTime.parse(close));
                                    break;
                                case "Friday":
                                    openingHoursResponse.setFridayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setFridayClose(LocalTime.parse(close));
                                    break;
                                case "Saturday":
                                    openingHoursResponse.setSaturdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setSaturdayClose(LocalTime.parse(close));
                                    break;
                                case "Sunday":
                                    openingHoursResponse.setSundayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setSundayClose(LocalTime.parse(close));
                                    break;
                            }
                        }
                    });

                    response.setOpeningHours(openingHoursResponse);
                }

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

    public RestaurantResponse getRestaurantResponseById(int id) {
        Connection connection = null;

        boolean exception = false;

        String query = "SELECT \n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        nr.ADDRESS_ID,\n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET, \n" +
                "        na.MUNICIPALITY ,\n" +
                "        nr.MANAGER_ID,\n" +
                "        LISTAGG(DISTINCT  nc.id ||' ' || nc.name, ',') WITHIN GROUP (ORDER BY nc.NAME) AS categories,\n" +
                "        LISTAGG(DISTINCT  noh.DAY_OF_WEEK  ||' ' || noh.OPENING_TIME || '-' || noh.CLOSING_TIME , ',') WITHIN GROUP (ORDER BY noh.DAY_OF_WEEK) AS opening_times,\n" +
                "        COALESCE(AVG(nr2.rating), 0) AS rating, \n" +
                "        COUNT(DISTINCT nr2.ID) AS customers_rated, \n" +
                "        COUNT(DISTINCT nfr.ID) AS customers_favorited\n" +
                "        \n" +
                "     FROM \n" +
                "        NBP_RESTAURANT nr\n" +
                "    LEFT JOIN \n" +
                "        NBP_ADDRESS na ON nr.ADDRESS_ID = na.ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_REVIEW nr2 ON nr.ID = nr2.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_FAVORITE_RESTAURANT nfr ON nr.ID = nfr.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_OPENING_HOURS noh ON nr.ID = noh.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "    \tNBP_RESTAURANT_CATEGORY nrc ON nrc.RESTAURANT_ID = nr.ID \n" +
                "    LEFT JOIN \n" +
                "    \t NBP_CATEGORY nc ON nc.ID = nrc.CATEGORY_ID \n"  +
                "\n WHERE nr.id = ?" +
                "GROUP BY \n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        nr.ADDRESS_ID,\n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET,\n" +
                "        na.MUNICIPALITY,\n" +
                "        nr.MANAGER_ID";




        try {
            connection = dbConnectionService.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);


            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();

            RestaurantResponse response = null;


            if (resultSet.next()) {
                response = new RestaurantResponse(resultSet.getInt("id"), resultSet.getString("name"),
                        new AddressResponse(resultSet.getInt("address_id"),resultSet.getString("street"),
                                resultSet.getString("municipality"), resultSet.getString("map_coordinates")),
                        resultSet.getString("logo"),
                        resultSet.getInt("manager_id") ,
                        null,
                        null,
                        resultSet.getFloat("rating"),
                        resultSet.getInt("customers_rated"),
                        resultSet.getInt("customers_favorited"));

                List<String> categories = resultSet.getString("categories") != null ?
                        Arrays.asList(resultSet.getString("categories").split(",")) : new ArrayList<>();

                if(!categories.isEmpty()) {
                    List<CategoryResponse> categoryResponses = new ArrayList<>();

                    categories.forEach(c -> {
                        Pattern pattern = Pattern.compile("(\\d+)\\s(.+)");
                        Matcher matcher = pattern.matcher(c);
                        if (matcher.find()) {
                            int catId = Integer.parseInt(matcher.group(1));
                            String name = matcher.group(2);
                            CategoryResponse categoryResponse= new CategoryResponse(catId,name);
                            categoryResponses.add(categoryResponse);
                        }
                    });
                    response.setCategories(categoryResponses);
                }

                List<String> openingTimes = resultSet.getString("opening_times") != null ?
                        Arrays.asList(resultSet.getString("opening_times").split(",")) : new ArrayList<>();

                if(!openingTimes.isEmpty()) {
                    OpeningHoursResponse openingHoursResponse = new OpeningHoursResponse();
                    openingTimes.forEach(ot -> {
                        Pattern pattern = Pattern.compile("([a-zA-Z]+)\\s(\\d{2}:\\d{2})-(\\d{2}:\\d{2})");
                        Matcher matcher = pattern.matcher(ot);
                        if (matcher.find()) {
                            String day = matcher.group(1);
                            String open = matcher.group(2);
                            String close = matcher.group(3);

                            switch (day){
                                case "Monday":
                                    openingHoursResponse.setMondayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setMondayClose(LocalTime.parse(close));
                                    break;
                                case "Tuesday":
                                    openingHoursResponse.setTuesdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setTuesdayClose(LocalTime.parse(close));
                                    break;
                                case "Wednesday":
                                    openingHoursResponse.setWednesdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setWednesdayClose(LocalTime.parse(close));
                                    break;
                                case "Thursday":
                                    openingHoursResponse.setThursdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setThursdayClose(LocalTime.parse(close));
                                    break;
                                case "Friday":
                                    openingHoursResponse.setFridayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setFridayClose(LocalTime.parse(close));
                                    break;
                                case "Saturday":
                                    openingHoursResponse.setSaturdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setSaturdayClose(LocalTime.parse(close));
                                    break;
                                case "Sunday":
                                    openingHoursResponse.setSundayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setSundayClose(LocalTime.parse(close));
                                    break;
                            }
                        }
                    });

                    response.setOpeningHours(openingHoursResponse);
                }

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

    public RestaurantPaginatedResponse getRestaurantsWithCategories(PaginatedRequest paginatedRequest, List<Integer> categoryIds) {
        Connection connection = null;

        boolean exception = false;

        String query = "SELECT * FROM (    SELECT ROW_NUMBER() OVER (ORDER BY nr.id) rnum," +
                " COUNT(DISTINCT nr.id) OVER () RESULT_COUNT,\n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        nr.ADDRESS_ID,\n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET, \n" +
                "        na.MUNICIPALITY ,\n" +
                "        nr.MANAGER_ID,\n" +
                "        LISTAGG(DISTINCT  nc.id ||' ' || nc.name, ',') WITHIN GROUP (ORDER BY nc.NAME) AS categories,\n" +
                "        LISTAGG(DISTINCT  noh.DAY_OF_WEEK  ||' ' || noh.OPENING_TIME || '-' || noh.CLOSING_TIME , ',') WITHIN GROUP (ORDER BY noh.DAY_OF_WEEK) AS opening_times,\n" +
                "        COALESCE(AVG(nr2.rating), 0) AS rating, \n" +
                "        COUNT(DISTINCT nr2.ID) AS customers_rated, \n" +
                "        COUNT(DISTINCT nfr.ID) AS customers_favorited\n" +
                "        \n" +
                "     FROM \n" +
                "        NBP_RESTAURANT nr\n" +
                "    LEFT JOIN \n" +
                "        NBP_ADDRESS na ON nr.ADDRESS_ID = na.ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_REVIEW nr2 ON nr.ID = nr2.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_FAVORITE_RESTAURANT nfr ON nr.ID = nfr.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "        NBP_OPENING_HOURS noh ON nr.ID = noh.RESTAURANT_ID \n" +
                "    LEFT JOIN \n" +
                "    \tNBP_RESTAURANT_CATEGORY nrc ON nrc.RESTAURANT_ID = nr.ID \n" +
                "    LEFT JOIN \n" +
                "    \t NBP_CATEGORY nc ON nc.ID = nrc.CATEGORY_ID \n" +
                "   WHERE EXISTS (SELECT nrc2.id FROM NBP_RESTAURANT_CATEGORY nrc2 WHERE nrc2.category_id IN" +
                " ("+ "?,".repeat(categoryIds.size() - 1) + "?) AND nrc2.restaurant_id=nr.id)" +
                "GROUP BY \n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        nr.ADDRESS_ID,\n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET,\n" +
                "        na.MUNICIPALITY,\n" +
                "        nr.MANAGER_ID\n" +
                "        \n" +
                "ORDER BY nr.id\n" +
                "        )\n" +
                "\n" +
                "WHERE \n" +
                "    rnum >= ?\n" +
                "    AND rnum <= ?";



        int offset = (paginatedRequest.getPage()-1)*paginatedRequest.getRecordsPerPage();

        try {
            connection = dbConnectionService.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            int paramIndex = 1;

            for (Integer categoryId : categoryIds) {
                preparedStatement.setInt(paramIndex, categoryId);
                paramIndex++;
            }

            preparedStatement.setInt(paramIndex,offset +1);
            paramIndex++;
            preparedStatement.setInt(paramIndex,offset + paginatedRequest.getRecordsPerPage());

            ResultSet resultSet = preparedStatement.executeQuery();

            RestaurantPaginatedResponse response = new RestaurantPaginatedResponse();
            response.setRestaurants(new ArrayList<>());
            response.setCurrentPage(paginatedRequest.getPage());


            while (resultSet.next()) {
                var restaurant = new RestaurantResponse(resultSet.getInt("id"), resultSet.getString("name"),
                        new AddressResponse(resultSet.getInt("address_id"),resultSet.getString("street"),
                                resultSet.getString("municipality"), resultSet.getString("map_coordinates")),
                        resultSet.getString("logo"),
                        resultSet.getInt("manager_id") ,
                        null,
                        null,
                        resultSet.getFloat("rating"),
                        resultSet.getInt("customers_rated"),
                        resultSet.getInt("customers_favorited"));

                List<String> categories = resultSet.getString("categories") != null ?
                        Arrays.asList(resultSet.getString("categories").split(",")) : new ArrayList<>();

                if(!categories.isEmpty()) {
                    List<CategoryResponse> categoryResponses = new ArrayList<>();

                    categories.forEach(c -> {
                        Pattern pattern = Pattern.compile("(\\d+)\\s(.+)");
                        Matcher matcher = pattern.matcher(c);
                        if (matcher.find()) {
                            int id = Integer.parseInt(matcher.group(1));
                            String name = matcher.group(2);
                            CategoryResponse categoryResponse= new CategoryResponse(id,name);
                            categoryResponses.add(categoryResponse);
                        }
                    });
                    restaurant.setCategories(categoryResponses);
                }

                List<String> openingTimes = resultSet.getString("opening_times") != null ?
                        Arrays.asList(resultSet.getString("opening_times").split(",")) : new ArrayList<>();

                if(!openingTimes.isEmpty()) {
                    OpeningHoursResponse openingHoursResponse = new OpeningHoursResponse();
                    openingTimes.forEach(ot -> {
                        Pattern pattern = Pattern.compile("([a-zA-Z]+)\\s(\\d{2}:\\d{2})-(\\d{2}:\\d{2})");
                        Matcher matcher = pattern.matcher(ot);
                        if (matcher.find()) {
                            String day = matcher.group(1);
                            String open = matcher.group(2);
                            String close = matcher.group(3);

                            switch (day){
                                case "Monday":
                                    openingHoursResponse.setMondayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setMondayClose(LocalTime.parse(close));
                                    break;
                                case "Tuesday":
                                    openingHoursResponse.setTuesdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setTuesdayClose(LocalTime.parse(close));
                                    break;
                                case "Wednesday":
                                    openingHoursResponse.setWednesdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setWednesdayClose(LocalTime.parse(close));
                                    break;
                                case "Thursday":
                                    openingHoursResponse.setThursdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setThursdayClose(LocalTime.parse(close));
                                    break;
                                case "Friday":
                                    openingHoursResponse.setFridayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setFridayClose(LocalTime.parse(close));
                                    break;
                                case "Saturday":
                                    openingHoursResponse.setSaturdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setSaturdayClose(LocalTime.parse(close));
                                    break;
                                case "Sunday":
                                    openingHoursResponse.setSundayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setSundayClose(LocalTime.parse(close));
                                    break;
                            }
                        }
                    });

                    restaurant.setOpeningHours(openingHoursResponse);

                    response.setTotalPages((resultSet.getInt("result_count") + paginatedRequest.getRecordsPerPage()-1)/paginatedRequest.getRecordsPerPage());
                    response.getRestaurants().add(restaurant);
                }

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

    public RestaurantPaginatedShortResponse getFavoriteRestaurants(PaginatedRequest paginatedRequest, String username) {
        Connection connection = null;

        boolean exception = false;

        String query = "SELECT * FROM (" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY nr.id) rnum, " +
                "COUNT(*) OVER () RESULT_COUNT,\n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET, \n" +
                "LISTAGG(DISTINCT nc.name, ',') WITHIN GROUP (ORDER BY nc.NAME) AS categories," +
                "        COALESCE(AVG(nr2.rating), 0) AS rating, \n" +
                "        COUNT(DISTINCT nr2.ID) AS customers_rated, \n" +
                "        COUNT(DISTINCT nfr.ID) AS customers_favorited, \n" +
                "        CASE \n" +
                "            WHEN TO_CHAR(SYSDATE, 'HH24:MI') >= noh.opening_time \n" +
                "                 AND TO_CHAR(SYSDATE, 'HH24:MI') <= noh.closing_time THEN 1 \n" +
                "            ELSE 0 \n" +
                "        END AS is_open,\n" +
                "1 AS customer_favorite" +
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
                "                                )" +
                " LEFT JOIN NBP_RESTAURANT_CATEGORY nrc ON nr.id = nrc.restaurant_id " +
                " LEFT JOIN NBP_CATEGORY nc ON nc.id=nrc.category_id " +
                " WHERE nr.id IN (SELECT restaurant_id FROM NBP_FAVORITE_RESTAURANT WHERE customer_id=?)" +
                "GROUP BY \n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET, \n" +
                "        CASE \n" +
                "            WHEN TO_CHAR(SYSDATE, 'HH24:MI') >= noh.opening_time \n" +
                "                 AND TO_CHAR(SYSDATE, 'HH24:MI') <= noh.closing_time THEN 1 \n" +
                "            ELSE 0 \n" +
                "        END) "  +
                " WHERE \n" +
                "    rnum >= ?\n" +
                "    AND rnum <= ?";


        int paramIndex = 1;

        int offset = (paginatedRequest.getPage()-1)*paginatedRequest.getRecordsPerPage();

        User user = userRepository.getByUsername(username);

        int userId = user != null ? user.getId() : -1;

        try {
            connection = dbConnectionService.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(paramIndex,userId);
            paramIndex +=1;


            preparedStatement.setInt(paramIndex,offset +1);
            paramIndex += 1;
            preparedStatement.setInt(paramIndex,offset + paginatedRequest.getRecordsPerPage());


            ResultSet resultSet = preparedStatement.executeQuery();

            RestaurantPaginatedShortResponse response = new RestaurantPaginatedShortResponse();
            response.setRestaurants(new ArrayList<>());
            response.setCurrentPage(paginatedRequest.getPage());

            while (resultSet.next()) {
                RestaurantShortResponse r = new RestaurantShortResponse(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("street"),
                        resultSet.getString("logo"),
                        resultSet.getInt("is_open") != 0,
                        resultSet.getString("map_coordinates"),
                        resultSet.getString("categories") != null ?
                                new TreeSet<>(Arrays.asList(resultSet.getString("categories").split(",")))
                                : new TreeSet<String>(),
                        resultSet.getFloat("rating"),
                        resultSet.getInt("customers_rated"),
                        resultSet.getInt("customers_favorited"),
                        resultSet.getInt("customer_favorite")>0);


                response.setTotalPages((resultSet.getInt("result_count") + paginatedRequest.getRecordsPerPage()-1)/paginatedRequest.getRecordsPerPage());
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


    public RestaurantResponse setRestaurantCategories(int id, List<Integer> categoryIds) {
        Connection connection = null;

        boolean exception = false;
        try {
            connection = dbConnectionService.getConnection();

            String procedureCall = "{call update_restaurant_categories(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement callableStatement = connection.prepareCall(procedureCall);

            callableStatement.setInt(1, id);
            callableStatement.setString(2,categoryIds.stream()
                                                        .map(Object::toString)
                                                        .collect(Collectors.joining(",")));

            callableStatement.registerOutParameter(3, Types.INTEGER);
            callableStatement.registerOutParameter(4, Types.VARCHAR);
            callableStatement.registerOutParameter(5, Types.VARCHAR);
            callableStatement.registerOutParameter(6, Types.INTEGER);
            callableStatement.registerOutParameter(7, Types.VARCHAR);
            callableStatement.registerOutParameter(8, Types.VARCHAR);
            callableStatement.registerOutParameter(9, Types.VARCHAR);
            callableStatement.registerOutParameter(10, Types.INTEGER);
            callableStatement.registerOutParameter(11, Types.VARCHAR);
            callableStatement.registerOutParameter(12, Types.VARCHAR);
            callableStatement.registerOutParameter(13, Types.FLOAT);
            callableStatement.registerOutParameter(14, Types.INTEGER);
            callableStatement.registerOutParameter(15, Types.INTEGER);

            callableStatement.execute();


            var response = new RestaurantResponse(id, callableStatement.getString(4),
                        new AddressResponse(callableStatement.getInt(6),callableStatement.getString(8),
                                callableStatement.getString(9), callableStatement.getString(7)),
                        callableStatement.getString(5),
                        callableStatement.getInt(10) ,
                        null,
                        null,
                        callableStatement.getFloat(13),
                        callableStatement.getInt(14),
                        callableStatement.getInt(15));

                List<String> categories = callableStatement.getString(11) != null ?
                        Arrays.asList(callableStatement.getString(11).split(",")) : new ArrayList<>();

                if(!categories.isEmpty()) {
                    List<CategoryResponse> categoryResponses = new ArrayList<>();

                    categories.forEach(c -> {
                        Pattern pattern = Pattern.compile("(\\d+)\\s(.+)");
                        Matcher matcher = pattern.matcher(c);
                        if (matcher.find()) {
                            int category_id = Integer.parseInt(matcher.group(1));
                            String category_name = matcher.group(2);
                            CategoryResponse categoryResponse= new CategoryResponse(category_id,category_name);
                            categoryResponses.add(categoryResponse);
                        }
                    });
                    response.setCategories(categoryResponses);
                }

                List<String> openingTimes = callableStatement.getString(12) != null ?
                        Arrays.asList(callableStatement.getString(12).split(",")) : new ArrayList<>();

                if(!openingTimes.isEmpty()) {
                    OpeningHoursResponse openingHoursResponse = new OpeningHoursResponse();
                    openingTimes.forEach(ot -> {
                        Pattern pattern = Pattern.compile("([a-zA-Z]+)\\s(\\d{2}:\\d{2})-(\\d{2}:\\d{2})");
                        Matcher matcher = pattern.matcher(ot);
                        if (matcher.find()) {
                            String day = matcher.group(1);
                            String open = matcher.group(2);
                            String close = matcher.group(3);

                            switch (day){
                                case "Monday":
                                    openingHoursResponse.setMondayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setMondayClose(LocalTime.parse(close));
                                    break;
                                case "Tuesday":
                                    openingHoursResponse.setTuesdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setTuesdayClose(LocalTime.parse(close));
                                    break;
                                case "Wednesday":
                                    openingHoursResponse.setWednesdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setWednesdayClose(LocalTime.parse(close));
                                    break;
                                case "Thursday":
                                    openingHoursResponse.setThursdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setThursdayClose(LocalTime.parse(close));
                                    break;
                                case "Friday":
                                    openingHoursResponse.setFridayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setFridayClose(LocalTime.parse(close));
                                    break;
                                case "Saturday":
                                    openingHoursResponse.setSaturdayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setSaturdayClose(LocalTime.parse(close));
                                    break;
                                case "Sunday":
                                    openingHoursResponse.setSundayOpen(LocalTime.parse(open));
                                    openingHoursResponse.setSundayClose(LocalTime.parse(close));
                                    break;
                            }
                        }
                    });

                    response.setOpeningHours(openingHoursResponse);
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

    private String constructQuery(FilterRestaurantRequest filter,String sortBy,boolean ascending, int id) {
        String baseQuery = "SELECT * FROM (" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY nr.id) rnum, " +
                "COUNT(*) OVER () RESULT_COUNT,\n" +
                "        nr.ID, \n" +
                "        nr.NAME, \n" +
                "        nr.LOGO, \n" +
                "        na.MAP_COORDINATES, \n" +
                "        na.STREET, \n" +
                "LISTAGG(DISTINCT nc.name, ',') WITHIN GROUP (ORDER BY nc.NAME) AS categories," +
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
                "                                )" +
                " LEFT JOIN NBP_RESTAURANT_CATEGORY nrc ON nr.id = nrc.restaurant_id " +
                " LEFT JOIN NBP_CATEGORY nc ON nc.id=nrc.category_id ";


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
                "    rnum >= ?\n" +
                "    AND rnum <= ?";

        if(id > -1){
            whereClause = "WHERE nr.id=?";
        }

        if(filter != null) {
            if(filter.getCategoryIds() != null && !filter.getCategoryIds().isEmpty()) {

                StringJoiner joiner = new StringJoiner(",", "", "");
                for (int i = 0; i < filter.getCategoryIds().size(); i++) {
                    joiner.add("?");
                }

                String categories = "(" + joiner + ")";
                whereClause = "WHERE nrc.category_id IN " + categories;
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
