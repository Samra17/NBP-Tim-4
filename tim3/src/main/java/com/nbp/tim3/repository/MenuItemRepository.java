package com.nbp.tim3.repository;

import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.model.MenuItem;
import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class MenuItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(MenuRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public MenuItem findById(int id) {
        String sql = "SELECT * FROM nbp_menu_item WHERE id=? AND is_deleted=0";

        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Double price = resultSet.getDouble("price");
                Double discountPrice = resultSet.getDouble("discount_price");
                if(resultSet.wasNull())
                    discountPrice = null;
                String image = resultSet.getString("image");
                Integer prepTime = resultSet.getInt("prep_time");
                int menuId = resultSet.getInt("menu_id");
                MenuItem menuItem = new MenuItem(id,name, description, price, discountPrice, image, prepTime, menuId);
                return  menuItem;
            }
            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteMenuItem(int id)  {
        Connection connection = null;
        var exception = false;
        try  {
            connection = dbConnectionService.getConnection();
            String sqlQuery = "UPDATE nbp_menu_item SET is_deleted = 1 WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            int rowCount = preparedStatement.executeUpdate();

            connection.commit();

            if (rowCount > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error(String.format("Deleting a menu item failed: %s", e.getMessage()));
            exception = true;
            return false;
        }
        finally {
            if(exception && connection!=null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public int updateMenuItem(MenuItemDto menuItem, int id) throws SQLException {
        String sql = "UPDATE nbp_menu_item SET name=?, description=?, price=?, discount_price=?, prep_time=?, image=? WHERE id=?";
        String sql2 = "SELECT COUNT(*) FROM nbp_menu_item WHERE id=? AND is_deleted=0";
        boolean exception = false;
        Connection connection = null;
        try {
            connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setInt(1, id);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            if (resultSet2.next()) {
                int rowCount2 = resultSet2.getInt(1);
                if (rowCount2 != 1)
                    throw new InvalidRequestException(String.format("Menu item with id %d does not exist!", id));
            }

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,menuItem.getName());
            preparedStatement.setString(2, menuItem.getDescription());
            if(menuItem.getDiscount_price() == null)
                preparedStatement.setNull(4, Types.FLOAT);
            else
                preparedStatement.setDouble(4, menuItem.getDiscount_price());
            if(menuItem.getPrep_time() == null)
                preparedStatement.setNull(5, Types.NUMERIC);
            else
                preparedStatement.setDouble(5, menuItem.getPrep_time());
            preparedStatement.setDouble(3, menuItem.getPrice());
            preparedStatement.setString(6, menuItem.getImage());
            preparedStatement.setInt(7, id);

            int rowCount = preparedStatement.executeUpdate();

            connection.commit();

            logger.info(String.format("Successfully updated %d rows into menu_item.", rowCount));

            return rowCount;
        }
        catch (Exception e) {
            logger.error(String.format("Updating a menu item failed: %s", e.getMessage()));
            exception=true;
            throw e;
        }
        finally {
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
