package com.nbp.tim3;

import com.nbp.tim3.repository.CategoryRepository;
import com.nbp.tim3.service.DBConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class Tim3Application {


	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(Tim3Application.class, args);

		DBConnectionService dbConnectionService = context.getBean(DBConnectionService.class);

		dbConnectionService.startDBConnection();

	}

}
