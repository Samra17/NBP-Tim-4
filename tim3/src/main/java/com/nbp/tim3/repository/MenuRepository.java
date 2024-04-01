package com.nbp.tim3.repository;

import com.nbp.tim3.dto.menu.MenuCreateRequest;
import com.nbp.tim3.dto.menu.MenuDto;
import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.dto.menu.MenuUpdateDto;
import com.nbp.tim3.model.Category;
import com.nbp.tim3.model.Menu;
import com.nbp.tim3.model.MenuItem;
import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MenuRepository {
    private static final Logger logger = LoggerFactory.getLogger(MenuRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public Menu findById(int id) {
        String sql = "SELECT * FROM nbp_menu WHERE id=?";

        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
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
                    if(resultSetMenuItems.wasNull())
                        menuItemDiscountPrice=null;
                    String menuItemImage = resultSetMenuItems.getString("image");
                    Integer menuItemPrepTime = resultSetMenuItems.getInt("prep_time");
                    MenuItem menuItem = new MenuItem(menuItemID,menuItemName, menuItemDescription, menuItemPrice, menuItemDiscountPrice, menuItemImage, menuItemPrepTime, id);
                    menuItems.add(menuItem);
                }
                Menu menu = new Menu(id, name, active,restaurantId, menuItems );
                return menu;
            }
            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<Menu> getActiveMenusForRestaurant(int restaurantID) {
        try {
            String sql = "SELECT * FROM nbp_menu WHERE active = 1 AND restaurant_id=?";
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
                    if(resultSetMenuItems.wasNull())
                        menuItemDiscountPrice = null;
                    String menuItemImage = resultSetMenuItems.getString("image");
                    Integer menuItemPrepTime = resultSetMenuItems.getInt("prep_time");
                    MenuItem menuItem = new MenuItem(menuItemID,menuItemName, menuItemDescription, menuItemPrice, menuItemDiscountPrice, menuItemImage, menuItemPrepTime, id);
                    menuItems.add(menuItem);
                }
                Menu menu = new Menu(id, name, active,restaurantId, menuItems );
                menus.add(menu);

            }
            return menus;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<MenuDto> getMenusForRestaurant(int restaurantID) {
        try {
            String sql = "SELECT * FROM nbp_menu WHERE  restaurant_id=?";
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantID);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<MenuDto> menus = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("name");
                boolean active = resultSet.getBoolean("active");
                int restaurantId = resultSet.getInt("restaurant_id");
                MenuDto menu = new MenuDto(id, active, restaurantId, name );
                menus.add(menu);

            }
            return menus;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addMenu(MenuDto menu) {
        String sql = "INSERT INTO nbp_menu(name, active, restaurant_id) VALUES(?,?,?)";
        String sql1 = "SELECT COUNT(*) FROM nbp_restaurant WHERE id=?";
        String sql2 = "SELECT COUNT(*) FROM nbp_menu WHERE restaurant_id=? AND name=?";

        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            preparedStatement1.setInt(1, menu.getRestaurantID());

            ResultSet resultSet1 = preparedStatement1.executeQuery();
            if(resultSet1.next()) {
                int rowCount1 = resultSet1.getInt(1);
                if (rowCount1 == 0)
                    throw new InvalidRequestException(String.format("Restaurant with id %d does not exist!", menu.getRestaurantID()));

                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setInt(1, menu.getRestaurantID());
                preparedStatement2.setString(2, menu.getName());
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                if (resultSet2.next()) {
                    int rowCount2 = resultSet2.getInt(1);
                    if (rowCount2 != 0)
                        throw new InvalidRequestException(String.format("Name %s already in use!", menu.getName()));

                    String returnCols[] = { "id" };
                    PreparedStatement preparedStatement = connection.prepareStatement(sql,returnCols);
                    preparedStatement.setString(1, menu.getName());
                    preparedStatement.setInt(2, menu.isActive() ? 1 : 0);
                    preparedStatement.setInt(3, menu.getRestaurantID());

                    int rowCount = preparedStatement.executeUpdate();

                    connection.commit();

                    if(rowCount > 0) {
                        ResultSet rs = preparedStatement.getGeneratedKeys();
                        if(rs.next()) {
                            int generatedId = rs.getInt(1);
                            menu.setId(generatedId);
                        } else {
                            logger.error("No generated ID!");
                        }
                    }

                    logger.info(String.format("Successfully inserted %d rows into Menu.", rowCount));
                }

                }
        }

        catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(String.format("Creating new menu failed: %s", e.getMessage()));
            throw e;
        }
    }

    public boolean deleteMenu(int id) {
        try  {
            Connection connection = dbConnectionService.getConnection();
            String sqlQuery = "DELETE FROM nbp_menu WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            connection.commit();

            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            logger.error(String.format("Deleting a menu failed: %s", e.getMessage()));
            return  false;
        }
    }

    public int updateMenu(MenuCreateRequest menu, int id) throws SQLException {
        String sql = "UPDATE nbp_menu SET name=?, active=? WHERE id=?";
        String sql2 = "SELECT COUNT(*) FROM nbp_menu WHERE restaurant_id=? AND name=? AND id!=?";

        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setInt(1, menu.getRestaurantID());
            preparedStatement2.setString(2, menu.getName());
            preparedStatement2.setInt(3, id);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            if (resultSet2.next()) {
                int rowCount2 = resultSet2.getInt(1);
                if (rowCount2 != 0)
                    throw new InvalidRequestException(String.format("Name %s already in use!", menu.getName()));
            }

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, menu.getName());
            preparedStatement.setInt(2, menu.isActive() ? 1 : 0);
            preparedStatement.setInt(3, id);

            int rowCount = preparedStatement.executeUpdate();

            connection.commit();

            logger.info(String.format("Successfully updated %d rows into Menu.", rowCount));

            return rowCount;
        }
        catch (Exception e) {
            logger.error(String.format("Creating new menu failed: %s", e.getMessage()));
            throw e;
        }
    }

    public Integer findMenuRestaurant(int id) {
        String sql = "SELECT * FROM nbp_menu WHERE id=?";
        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Integer restaurantId = resultSet.getInt("restaurant_id");
                return restaurantId;
            }
            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addMenuItemsToMenu(int id, List<MenuItemDto> menuItemsDao) {
        boolean exception = false;
        Connection connection = null;
        String returnCols[] = { "id" };
        String sql = "INSERT INTO nbp_menu_item(name, description, price, discount_price, prep_time, image, menu_id) VALUES(?,?,?,?,?,?,?)";

        try {
            connection = dbConnectionService.getConnection();

            for(var menuItemDao: menuItemsDao) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql,returnCols);
                preparedStatement.setString(1,menuItemDao.getName());
                preparedStatement.setString(2, menuItemDao.getDescription());
                if(menuItemDao.getDiscount_price() == null)
                    preparedStatement.setNull(4, Types.FLOAT);
                else
                    preparedStatement.setDouble(4, menuItemDao.getDiscount_price());
                if(menuItemDao.getPrep_time() == null)
                    preparedStatement.setNull(5, Types.NUMERIC);
                else
                    preparedStatement.setDouble(5, menuItemDao.getPrep_time());
                preparedStatement.setDouble(3, menuItemDao.getPrice());
                preparedStatement.setString(6, menuItemDao.getImage());
                preparedStatement.setInt(7, id);
                int rowCount = preparedStatement.executeUpdate();
                if(rowCount > 0) {
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    if(!rs.next()) {
                        logger.error("No generated ID!");
                    }
                }
            }
        connection.commit();
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            exception = true;
            if(e.getSQLState().startsWith("23")) {
                if (e.getMessage().contains("FK_MENU_ITEM_MENU")) {
                    throw new InvalidRequestException(String.format("Menu with id %d does not exist!", id));
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
