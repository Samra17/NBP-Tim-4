package com.nbp.tim3.repository;

import com.nbp.tim3.model.Address;
import com.nbp.tim3.model.Role;
import com.nbp.tim3.service.DBConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Repository
public class AddressRepository {
    @Autowired
    DBConnectionService dbConnectionService;

    public Address getById(int id) {
        String sql = "SELECT * FROM nbp_address WHERE id=?";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                String street = resultSet.getString("street");
                String municipality = resultSet.getString("municipality");
                String mapCoordinates = resultSet.getString("map_coordinates");


                return new Address(id, street, municipality,mapCoordinates);
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
}
