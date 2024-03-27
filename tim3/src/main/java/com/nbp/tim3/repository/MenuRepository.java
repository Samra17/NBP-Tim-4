package com.nbp.tim3.repository;

import com.nbp.tim3.model.Category;
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
                    float menuItemPrice = resultSetMenuItems.getFloat("price");
                    float menuItemDiscountPrice = resultSetMenuItems.getFloat("discount_price");
                    String menuItemImage = resultSetMenuItems.getString("image");
                    int menuItemPrepTime = resultSetMenuItems.getInt("prep_time");
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
}
