package com.nbp.tim3.repository;

import com.nbp.tim3.model.Address;
import com.nbp.tim3.model.Category;
import com.nbp.tim3.model.Role;
import com.nbp.tim3.model.User;
import com.nbp.tim3.service.DBConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class UserRepository {
    @Autowired
    DBConnectionService dbConnectionService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AddressRepository addressRepository;

    public User getById(int id) {
        String sql = "SELECT * FROM nbp.nbp_user WHERE id=?";

        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
        }
    }
}
