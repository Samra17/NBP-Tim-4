package com.nbp.tim3.repository;

import com.nbp.tim3.dto.menu.MenuDto;
import com.nbp.tim3.dto.menu.MenuItemDto;
import com.nbp.tim3.model.Menu;
import com.nbp.tim3.model.MenuItem;
import com.nbp.tim3.service.DBConnectionService;
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

@Repository
public class MenuItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(MenuRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public MenuItem findById(int id) {
        String sql = "SELECT * FROM nbp_menu_item WHERE id=?";

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

    public boolean deleteMenuItem(int id) {
        try  {
            Connection connection = dbConnectionService.getConnection();
            String sqlQuery = "DELETE FROM nbp_menu_item WHERE id = ?";
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
            logger.error(String.format("Deleting a menu item failed: %s", e.getMessage()));
            return  false;
        }
    }

    }
