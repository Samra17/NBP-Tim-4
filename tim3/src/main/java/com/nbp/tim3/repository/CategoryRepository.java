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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CategoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRepository.class);

    @Autowired
    DBConnectionService dbConnectionService;

    public void addCategory(Category category) {
        String sql = "INSERT INTO nbp_category(name) VALUES(?)";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            String returnCols[] = { "id" };
            preparedStatement = connection.prepareStatement(sql,returnCols);
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
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public int updateCategory(Category category) {
        String sql = "UPDATE nbp_category SET name=? WHERE id=?";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
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
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM nbp_category";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                Category category = new Category(id, name);
                categories.add(category);
            }

            return  categories;
        } catch (Exception e) {
            e.printStackTrace();
            return categories;
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Category getById(int id) {
        String sql = "SELECT * FROM nbp_category WHERE id=?";

        PreparedStatement preparedStatement = null;
        try {
            Connection connection = dbConnectionService.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                String name = resultSet.getString("name");

                return new Category(id, name);
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

    public boolean deleteCategory(int id) {
        PreparedStatement preparedStatement = null;
        try  {
            Connection connection = dbConnectionService.getConnection();
            String sqlQuery = "DELETE FROM nbp_category WHERE id = ?";
            preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            connection.commit();

            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
