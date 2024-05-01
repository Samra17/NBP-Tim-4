package com.nbp.tim3.repository;

import com.nbp.tim3.model.*;
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
public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AddressRepository addressRepository;

    public User getById(int id) {
        String sql = "SELECT * FROM nbp.nbp_user WHERE id=?";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String username = resultSet.getString("username");
                String phoneNumber = resultSet.getString("phone_number");

                User user = new User(id,firstName,lastName,email,password,username,phoneNumber,null,null);

                if(resultSet.getObject("role_id") != null) {
                    int roleId = resultSet.getInt("role_id");
                    Role role = roleRepository.getById(roleId);
                    user.setRole(role);
                }

                if(resultSet.getObject("address_id") != null) {
                    int addressId = resultSet.getInt("address_id");
                    Address address = addressRepository.getById(addressId);
                    user.setAddress(address);
                }

                return user;
            }

            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public User getByUsername(String username) {
        String sql = "SELECT * FROM nbp.nbp_user WHERE username=?";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String phoneNumber = resultSet.getString("phone_number");

                User user = new User(id,firstName,lastName,email,password,username,phoneNumber,null,null);

                if(resultSet.getObject("role_id") != null) {
                    int roleId = resultSet.getInt("role_id");
                    Role role = roleRepository.getById(roleId);
                    user.setRole(role);
                }

                if(resultSet.getObject("address_id") != null) {
                    int addressId = resultSet.getInt("address_id");
                    Address address = addressRepository.getById(addressId);
                    user.setAddress(address);
                }

                return user;
            }

            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addUser(User user, Address address)  {
        String sqlAdr = "INSERT INTO nbp_address(street,municipality,map_coordinates) VALUES (?,?,?)";
        String sqlUser = "";

        Connection connection = null;

        boolean exception = false;

        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();
            String returnCols[] = { "id" };

            if(address != null) {
                preparedStatement = connection.prepareStatement(sqlAdr, returnCols);
                preparedStatement.setString(1, address.getStreet());
                preparedStatement.setString(2, address.getMunicipality());
                preparedStatement.setString(3, address.getMapCoordinates());

                int rowCount = preparedStatement.executeUpdate();

                if (rowCount > 0) {
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        address.setId(generatedId);
                        user.setAddress(address);
                    } else {
                        logger.error("No generated ID!");
                    }
                }
                sqlUser = "INSERT INTO nbp.nbp_user(first_name, last_name, email, password, username,phone_number, role_id, address_id) VALUES(?,?,?,?,?,?,?,?)";
            } else {
                 sqlUser = "INSERT INTO nbp.nbp_user(first_name, last_name, email, password, username,phone_number, role_id) VALUES(?,?,?,?,?,?,?)";
            }

            preparedStatement = connection.prepareStatement(sqlUser,returnCols);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2,user.getLastName());
            preparedStatement.setString(3,user.getEmail());
            preparedStatement.setString(4,user.getPassword());
            preparedStatement.setString(5,user.getUsername());
            preparedStatement.setString(6,user.getPhoneNumber());
            preparedStatement.setInt(7,user.getRole().getId());

            if(address != null)
                preparedStatement.setInt(8,address.getId());


            int rowCount = preparedStatement.executeUpdate();

            if(rowCount > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if(rs.next()) {
                    int generatedId = rs.getInt(1);
                    user.setId(generatedId);
                } else {
                    logger.error("No generated ID!");
                }
            }

            connection.commit();


            logger.info(String.format("Successfully inserted %d rows into User.", rowCount));


        }

        catch (SQLException e) {
            logger.error(e.getMessage());

            exception = true;

            if(e.getSQLState().startsWith("23")) {
                if(e.getErrorCode() == 1) {
                    if (e.getMessage().contains("NBP_ADDRESS_UN"))
                        throw new InvalidRequestException(String.format("Map coordinates %s already in use!", address.getMapCoordinates()));
                    else
                        throw new InvalidRequestException("Username or email already in use!");
                }
            }


        } catch (Exception e) {
            exception = true;
            e.printStackTrace();
            throw e;
        } finally {
            if(exception && connection!=null) {
                try {
                    Objects.requireNonNull(preparedStatement).close();
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }

    public void updateUser(User user, Address address)  {
        String sqlSelectAdr = "SELECT * FROM nbp_address WHERE id = (SELECT address_id FROM nbp.nbp_user WHERE id = ?) FETCH FIRST 1 ROW ONLY";
        String sqlUser = "UPDATE nbp.nbp_user SET first_name=?,last_name=?,email=?,password=?,username=?,phone_number=?,address_id=? WHERE id=?";


        Connection connection = null;

        boolean exception = false;
        int rowCount = 0;

        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();
            String returnCols[] = { "id" };

            //Check whether user has an address
            preparedStatement = connection.prepareStatement(sqlSelectAdr);
            preparedStatement.setInt(1,user.getId());

            ResultSet rs = preparedStatement.executeQuery();


            if(address == null) {
                preparedStatement = connection.prepareStatement(sqlUser);
                preparedStatement.setString(1, user.getFirstName());
                preparedStatement.setString(2,user.getLastName());
                preparedStatement.setString(3,user.getEmail());
                preparedStatement.setString(4,user.getPassword());
                preparedStatement.setString(5,user.getUsername());
                preparedStatement.setString(6,user.getPhoneNumber());
                preparedStatement.setNull(7, Types.INTEGER);
                preparedStatement.setInt(8,user.getId());

                preparedStatement.executeUpdate();

                if(rs.next()) {
                    int id = rs.getInt("id");
                    //Delete existing address
                    String sqlDeleteAdr = "DELETE FROM nbp_address WHERE id=?";
                    preparedStatement = connection.prepareStatement(sqlDeleteAdr);
                    preparedStatement.setInt(1,id);

                    rowCount = preparedStatement.executeUpdate();

                    if(rowCount>0) {
                        logger.info(String.format("Deleted %d rows from Address!",rowCount));
                    }
                }

            } else {
                if(rs.next()) {
                    int id = rs.getInt("id");

                    //Update existing address
                    String sqlUpdateAdr = "UPDATE nbp_address SET street=?, municipality=?, map_coordinates=? WHERE id=?";
                    preparedStatement = connection.prepareStatement(sqlUpdateAdr);
                    preparedStatement.setString(1, address.getStreet());
                    preparedStatement.setString(2, address.getMunicipality());
                    preparedStatement.setString(3, address.getMapCoordinates());
                    preparedStatement.setInt(4, id);

                    rowCount = preparedStatement.executeUpdate();

                    if (rowCount > 0) {
                        address.setId(id);
                        logger.info(String.format("Updated %d rows in Address!",rowCount));
                    }
                } else {
                    //Insert new address
                    String sqlAdr = "INSERT INTO nbp_address(street,municipality,map_coordinates) VALUES (?,?,?)";
                    preparedStatement = connection.prepareStatement(sqlAdr, returnCols);
                    preparedStatement.setString(1, address.getStreet());
                    preparedStatement.setString(2, address.getMunicipality());
                    preparedStatement.setString(3, address.getMapCoordinates());

                    rowCount = preparedStatement.executeUpdate();

                    if (rowCount > 0) {
                        ResultSet resultSet = preparedStatement.getGeneratedKeys();
                        if (resultSet.next()) {
                            int generatedId = resultSet.getInt(1);
                            address.setId(generatedId);
                        } else {
                            logger.error("No generated ID!");
                        }
                    }
                }

                preparedStatement = connection.prepareStatement(sqlUser);
                preparedStatement.setString(1, user.getFirstName());
                preparedStatement.setString(2,user.getLastName());
                preparedStatement.setString(3,user.getEmail());
                preparedStatement.setString(4,user.getPassword());
                preparedStatement.setString(5,user.getUsername());
                preparedStatement.setString(6,user.getPhoneNumber());
                if(address!= null)
                    preparedStatement.setInt(7,address.getId());
                else
                    preparedStatement.setNull(7, Types.INTEGER);
                preparedStatement.setInt(8,user.getId());


                 rowCount = preparedStatement.executeUpdate();
            }


            connection.commit();


            logger.info(String.format("Successfully inserted %d rows into User.", rowCount));


        }

        catch (SQLException e) {
            logger.error(e.getMessage());

            exception = true;

            if(e.getSQLState().startsWith("23")) {
                if(e.getErrorCode() == 1) {
                    if (e.getMessage().contains("NBP_ADDRESS_UN"))
                        throw new InvalidRequestException(String.format("Map coordinates %s already in use!", address.getMapCoordinates()));
                    else
                        throw new InvalidRequestException("Username or email already in use!");
                }
            }


        } catch (Exception e) {
            exception = true;
            e.printStackTrace();
            throw e;
        } finally {
            if(exception && connection!=null) {
                try {
                    Objects.requireNonNull(preparedStatement).close();
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public List<User> getAll() {
        String sql = "SELECT * FROM nbp.nbp_user u JOIN nbp.nbp_role r ON u.role_id=r.id " +
                "WHERE r.name IN ('ADMINISTRATOR','COURIER','RESTAURANT_MANAGER','CUSTOMER')";
        List<User> users = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                User user = fillUserData(resultSet);
                users.add(user);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return users;
    }

    public List<User> getManagers() {
        String sql = "SELECT * FROM nbp.nbp_user u JOIN nbp.nbp_role r ON u.role_id=r.id " +
                "WHERE r.name = 'RESTAURANT_MANAGER'";
        List<User> users = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                User user = fillUserData(resultSet);
                users.add(user);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return users;
    }

    public List<User> getCouriers() {
        String sql = "SELECT * FROM nbp.nbp_user u JOIN nbp.nbp_role r ON u.role_id=r.id " +
                "WHERE r.name = 'COURIER'";
        List<User> users = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                User user = fillUserData(resultSet);
                users.add(user);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return users;
    }



    private User fillUserData(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String username = resultSet.getString("username");
        String phoneNumber = resultSet.getString("phone_number");

        User user = new User(id,firstName,lastName,email,password,username,phoneNumber,null,null);

        if(resultSet.getObject("role_id") != null) {
            int roleId = resultSet.getInt("role_id");
            Role role = roleRepository.getById(roleId);
            user.setRole(role);
        }

        if(resultSet.getObject("address_id") != null) {
            int addressId = resultSet.getInt("address_id");
            Address address = addressRepository.getById(addressId);
            user.setAddress(address);
        }
        return user;
    }
}
