package com.nbp.tim3.repository;

import com.nbp.tim3.model.Role;
import com.nbp.tim3.model.User;
import com.nbp.tim3.service.DBConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class RoleRepository {
    @Autowired
    DBConnectionService dbConnectionService;

    public Role getById(int id) {
        String sql = "SELECT * FROM nbp.nbp_role WHERE id=?";

        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                String name = resultSet.getString("name");


                return new Role(id, name);
            }

            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Role getByName(String name) {
        String sql = "SELECT * FROM nbp.nbp_role WHERE name=?";

        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                int id = resultSet.getInt("id");


                return new Role(id, name);
            }

            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
