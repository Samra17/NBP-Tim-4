package com.nbp.tim3.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.nbp.tim3.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DBConnectionService {

    private static final Logger logger = LoggerFactory.getLogger(DBConnectionService.class);

    @Value("${DB_USERNAME}")
    private String username;

    @Value("${DB_PASSWORD}")
    private String password;

    @Value("${DB_HOSTNAME}")
    private String hostname;

    @Value("${DB_SERVICE_NAME}")
    private String serviceName;

    private Connection connection;

    public void startDBConnection() {
        connection = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");

            String url = "jdbc:oracle:thin:@" + hostname + "/" + serviceName;
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            boolean isValid = connection.isValid(5);
            if (isValid) {
                logger.info("Database connection successfully established.");
            } else {
                logger.error("Database connection not established.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("Connection has not been established.");
        }
        return connection;
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connection = null;
            }
        }
    }
}