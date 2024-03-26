package com.nbp.tim3.repository;

import com.nbp.tim3.model.Category;
import com.nbp.tim3.service.DBConnectionService;
import com.nbp.tim3.util.exception.InvalidRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
public class CategoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public void addCategory(Category category) {
        String sql = "INSERT INTO nbp_category(name) VALUES(?)";

        try {
            Connection connection = dbConnectionService.getConnection();
            String returnCols[] = { "id" };
            PreparedStatement preparedStatement = connection.prepareStatement(sql,returnCols);
            preparedStatement.setString(1, category.getName());


            int rowCount = preparedStatement.executeUpdate();

            connection.commit();

            if(rowCount > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if(rs.next()) {
                    int generatedId = rs.getInt(1);
                    category.setId(generatedId);
                } else {
                    logger.error("No generated ID!");
                }
            }

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

    public int updateCategory(Category category) {
        String sql = "UPDATE nbp_category SET name=? WHERE id=?";

        try {
            Connection connection = dbConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getId());


            int rowCount = preparedStatement.executeUpdate();

            connection.commit();


            logger.info(String.format("Successfully updated %d rows into Category.", rowCount));

            return rowCount;
        }
        catch (SQLException e) {
            logger.error(e.getMessage());

            if(e.getSQLState().startsWith("23"))
                throw new InvalidRequestException(String.format("Category with name %s already exists!",category.getName()));
            return 0;
        }
         catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}
