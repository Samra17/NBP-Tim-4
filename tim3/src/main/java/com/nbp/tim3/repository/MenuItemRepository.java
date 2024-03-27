package com.nbp.tim3.repository;

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
                float price = resultSet.getFloat("price");
                float discountPrice = resultSet.getFloat("discount_price");
                String image = resultSet.getString("image");
                int prepTime = resultSet.getInt("prep_time");
                MenuItem menuItem = new MenuItem(id,name, description, price, discountPrice, image, prepTime, id);
                return  menuItem;
            }
            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
