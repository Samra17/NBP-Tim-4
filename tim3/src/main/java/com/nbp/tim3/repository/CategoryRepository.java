package com.nbp.tim3.repository;

import com.nbp.tim3.model.Category;
import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class CategoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public void addCategory(Category category) {
        String sql = "INSERT INTO nbp_category(name) VALUES(?)";

        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, category.getName());


            int rowCount = preparedStatement.executeUpdate();

            connection.commit();


            logger.info(String.format("Successfully inserted %d rows into Category.", rowCount));
        }

        catch (SQLException e) {
            logger.error(e.getMessage());

            if(e.getSQLState().startsWith("23"))
                throw new InvalidRequestException(String.format("Category with name %s already exists!",category.getName()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
