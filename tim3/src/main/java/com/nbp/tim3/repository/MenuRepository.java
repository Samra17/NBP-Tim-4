package com.nbp.tim3.repository;

import com.nbp.tim3.dto.menu.MenuCreateRequest;
import com.nbp.tim3.dto.menu.MenuDto;
import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.model.Menu;
import com.nbp.tim3.model.MenuItem;
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
public class MenuRepository {
    private static final Logger logger = LoggerFactory.getLogger(MenuRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public Menu findById(int id) {
        String sql = "SELECT * FROM nbp_menu WHERE id=? AND is_deleted=0";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                String name = resultSet.getString("name");
                boolean active = resultSet.getBoolean("active");
                int restaurantId = resultSet.getInt("restaurant_id");

                String sqlMenuItems = "SELECT * FROM nbp_menu_item WHERE menu_id=? AND is_deleted=0";
                PreparedStatement preparedStatementMenuItems = connection.prepareStatement(sqlMenuItems);
                preparedStatementMenuItems.setInt(1, id);

                ResultSet resultSetMenuItems = preparedStatementMenuItems.executeQuery();

                List<MenuItem> menuItems = new ArrayList<>();

                while (resultSetMenuItems.next()) {
                    int menuItemID = resultSetMenuItems.getInt("ID");
                    String menuItemName = resultSetMenuItems.getString("name");
                    String menuItemDescription = resultSetMenuItems.getString("description");
                    Double menuItemPrice = resultSetMenuItems.getDouble("price");

                    Double menuItemDiscountPrice = resultSetMenuItems.getDouble("discount_price");
                    if (resultSetMenuItems.wasNull())
                        menuItemDiscountPrice = null;
                    String menuItemImage = resultSetMenuItems.getString("image");
                    Integer menuItemPrepTime = resultSetMenuItems.getInt("prep_time");
                    MenuItem menuItem = new MenuItem(menuItemID, menuItemName, menuItemDescription, menuItemPrice, menuItemDiscountPrice, menuItemImage, menuItemPrepTime, id);
                    menuItems.add(menuItem);
                }
                Menu menu = new Menu(id, name, active, restaurantId, menuItems);
                return menu;
            }
            return null;
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

    public List<Menu> getActiveMenusForRestaurant(int restaurantID) {
        PreparedStatement preparedStatement = null;
        try {
            String sql = "SELECT * FROM nbp_menu WHERE active = 1 AND restaurant_id=? AND is_deleted=0";
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantID);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Menu> menus = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("name");
                boolean active = resultSet.getBoolean("active");
                int restaurantId = resultSet.getInt("restaurant_id");

                String sqlMenuItems = "SELECT * FROM nbp_menu_item WHERE menu_id=?";
                PreparedStatement preparedStatementMenuItems = connection.prepareStatement(sqlMenuItems);
                preparedStatementMenuItems.setInt(1, id);

                ResultSet resultSetMenuItems = preparedStatementMenuItems.executeQuery();

                List<MenuItem> menuItems = new ArrayList<>();

                while (resultSetMenuItems.next()) {
                    int menuItemID = resultSetMenuItems.getInt("ID");
                    String menuItemName = resultSetMenuItems.getString("name");
                    String menuItemDescription = resultSetMenuItems.getString("description");
                    Double menuItemPrice = resultSetMenuItems.getDouble("price");
                    Double menuItemDiscountPrice = resultSetMenuItems.getDouble("discount_price");
                    if (resultSetMenuItems.wasNull())
                        menuItemDiscountPrice = null;
                    String menuItemImage = resultSetMenuItems.getString("image");
                    Integer menuItemPrepTime = resultSetMenuItems.getInt("prep_time");
                    MenuItem menuItem = new MenuItem(menuItemID, menuItemName, menuItemDescription, menuItemPrice, menuItemDiscountPrice, menuItemImage, menuItemPrepTime, id);
                    menuItems.add(menuItem);
                }
                Menu menu = new Menu(id, name, active, restaurantId, menuItems);
                menus.add(menu);

            }
            return menus;
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

    public List<MenuDto> getMenusForRestaurant(int restaurantID) {
        PreparedStatement preparedStatement = null;
        try {
            String sql = "SELECT * FROM nbp_menu WHERE  restaurant_id=? AND is_deleted=0";
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantID);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<MenuDto> menus = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("name");
                boolean active = resultSet.getBoolean("active");
                int restaurantId = resultSet.getInt("restaurant_id");
                MenuDto menu = new MenuDto(id, active, restaurantId, name);
                menus.add(menu);

            }
            return menus;
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

    public void addMenu(MenuDto menu) {
        String sql = "INSERT INTO nbp_menu(name, active, restaurant_id) VALUES(?,?,?)";
        String sql1 = "SELECT COUNT(*) FROM nbp_restaurant WHERE id=?";
        String sql2 = "SELECT COUNT(*) FROM nbp_menu WHERE restaurant_id=? AND name=? AND is_deleted=0";
        var exception = false;
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();
            preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setInt(1, menu.getRestaurantID());

            ResultSet resultSet1 = preparedStatement1.executeQuery();
            if (resultSet1.next()) {
                int rowCount1 = resultSet1.getInt(1);
                if (rowCount1 == 0)
                    throw new InvalidRequestException(String.format("Restaurant with id %d does not exist!", menu.getRestaurantID()));

                preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setInt(1, menu.getRestaurantID());
                preparedStatement2.setString(2, menu.getName());
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                if (resultSet2.next()) {
                    int rowCount2 = resultSet2.getInt(1);
                    if (rowCount2 != 0)
                        throw new InvalidRequestException(String.format("Name %s already in use!", menu.getName()));

                    String returnCols[] = {"id"};
                    preparedStatement = connection.prepareStatement(sql, returnCols);
                    preparedStatement.setString(1, menu.getName());
                    preparedStatement.setInt(2, menu.isActive() ? 1 : 0);
                    preparedStatement.setInt(3, menu.getRestaurantID());

                    int rowCount = preparedStatement.executeUpdate();

                    connection.commit();

                    if (rowCount > 0) {
                        ResultSet rs = preparedStatement.getGeneratedKeys();
                        if (rs.next()) {
                            int generatedId = rs.getInt(1);
                            menu.setId(generatedId);
                        } else {
                            logger.error("No generated ID!");
                        }
                    }

                    logger.info(String.format("Successfully inserted %d rows into Menu.", rowCount));
                }

            }
        } catch (SQLException e) {
            exception = true;
            logger.error(e.getMessage());
        } catch (Exception e) {
            exception = true;
            logger.error(String.format("Creating new menu failed: %s", e.getMessage()));
            throw e;
        } finally {
            if (exception && connection != null) {
                try {
                    Objects.requireNonNull(preparedStatement).close();;
                    Objects.requireNonNull(preparedStatement1).close();;
                    Objects.requireNonNull(preparedStatement2).close();;
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public boolean deleteMenu(int id) {
        Connection connection = null;
        var exception = false;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();
            String sqlQuery = "UPDATE nbp_menu SET is_deleted = 1 WHERE id = ?";
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            int rowCount = preparedStatement.executeUpdate();

            sqlQuery = "UPDATE nbp_menu_item SET is_deleted = 1 WHERE menu_id = ?";
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            connection.commit();

            if (rowCount > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error(String.format("Deleting a menu failed: %s", e.getMessage()));
            exception = true;
            return false;
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


    public int updateMenu(MenuCreateRequest menu, int id) throws SQLException {
        String sql = "UPDATE nbp_menu SET name=?, active=? WHERE id=?";
        String sql2 = "SELECT COUNT(*) FROM nbp_menu WHERE restaurant_id=? AND name=? AND id!=? AND is_deleted=0";

        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setInt(1, menu.getRestaurantID());
            preparedStatement2.setString(2, menu.getName());
            preparedStatement2.setInt(3, id);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            if (resultSet2.next()) {
                int rowCount2 = resultSet2.getInt(1);
                if (rowCount2 != 0)
                    throw new InvalidRequestException(String.format("Name %s already in use!", menu.getName()));
            }

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, menu.getName());
            preparedStatement.setInt(2, menu.isActive() ? 1 : 0);
            preparedStatement.setInt(3, id);

            int rowCount = preparedStatement.executeUpdate();

            connection.commit();

            logger.info(String.format("Successfully updated %d rows into Menu.", rowCount));

            return rowCount;
        } catch (Exception e) {
            logger.error(String.format("Creating new menu failed: %s", e.getMessage()));
            throw e;
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Integer findMenuRestaurant(int id) {
        String sql = "SELECT * FROM nbp_menu WHERE id=? AND is_deleted=0";
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Integer restaurantId = resultSet.getInt("restaurant_id");
                return restaurantId;
            }
            return null;
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

    public void addMenuItemsToMenu(int id, List<MenuItemDto> menuItemsDao) {
        boolean exception = false;
        Connection connection = null;
        String returnCols[] = {"id"};
        String sql = "INSERT INTO nbp_menu_item(name, description, price, discount_price, prep_time, image, menu_id) VALUES(?,?,?,?,?,?,?)";
        String sql1 = "SELECT COUNT(*) FROM nbp_menu WHERE id=? AND is_deleted=0";

        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dbConnectionService.getConnection();

            preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setInt(1, id);

            ResultSet resultSet1 = preparedStatement1.executeQuery();
            if (resultSet1.next()) {
                int rowCount1 = resultSet1.getInt(1);
                if (rowCount1 == 0)
                    throw new InvalidRequestException(String.format("Menu with id %d does not exist!", id));
            }

            for (var menuItemDao : menuItemsDao) {
                preparedStatement = connection.prepareStatement(sql, returnCols);
                preparedStatement.setString(1, menuItemDao.getName());
                preparedStatement.setString(2, menuItemDao.getDescription());
                if (menuItemDao.getDiscountPrice() == null)
                    preparedStatement.setNull(4, Types.FLOAT);
                else
                    preparedStatement.setDouble(4, menuItemDao.getDiscountPrice());
                if (menuItemDao.getPrepTime() == null)
                    preparedStatement.setNull(5, Types.NUMERIC);
                else
                    preparedStatement.setDouble(5, menuItemDao.getPrepTime());
                preparedStatement.setDouble(3, menuItemDao.getPrice());
                preparedStatement.setString(6, menuItemDao.getImage());
                preparedStatement.setInt(7, id);
                int rowCount = preparedStatement.executeUpdate();
                if (rowCount > 0) {
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    if (!rs.next()) {
                        logger.error("No generated ID!");
                    }
                }
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            exception = true;
            if (e.getSQLState().startsWith("23")) {
                if (e.getMessage().contains("FK_MENU_ITEM_MENU")) {
                    throw new InvalidRequestException(String.format("Menu with id %d does not exist!", id));
                }
            }
        } catch (Exception e) {
            exception = true;
            logger.error(e.getMessage());
            throw e;
        } finally {
            if (exception && connection != null) {
                try {
                    Objects.requireNonNull(preparedStatement).close();
                    Objects.requireNonNull(preparedStatement1).close();
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
