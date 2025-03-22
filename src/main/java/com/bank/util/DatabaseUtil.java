package com.bank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static SessionFactory sessionFactory;
    private static final int MYSQL_PORT = 3308;

    public static void initialize() {
        try {
            // Test direct connection first
            testDirectConnection();
            
            // Then initialize Hibernate
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            
            // Log Hibernate configuration
            logger.info("Initializing Hibernate with configuration:");
            logger.info("URL: " + configuration.getProperty("hibernate.connection.url"));
            logger.info("Dialect: " + configuration.getProperty("hibernate.dialect"));
            
            sessionFactory = configuration.buildSessionFactory();
            logger.info("Database connection initialized successfully");
        } catch (Exception e) {
            logger.error("Error initializing database connection: " + e.getMessage(), e);
            throw new RuntimeException("Could not initialize database connection", e);
        }
    }

    private static void testDirectConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = String.format("jdbc:mysql://localhost:%d/bank_db?useSSL=false&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", MYSQL_PORT);
            
            logger.info("Attempting to connect to database with URL: " + url);
            
            conn = DriverManager.getConnection(url, "root", "");
            logger.info("Direct database connection test successful");
        } catch (ClassNotFoundException e) {
            logger.error("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        } catch (SQLException e) {
            logger.error("SQL Error during connection: " + e.getMessage());
            logger.error("SQL State: " + e.getSQLState());
            logger.error("Error Code: " + e.getErrorCode());
            throw new RuntimeException("SQL Error during connection", e);
        } catch (Exception e) {
            logger.error("Direct database connection test failed: " + e.getMessage(), e);
            throw new RuntimeException("Direct database connection test failed", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public static Session getSession() {
        if (sessionFactory == null) {
            initialize();
        }
        return sessionFactory.openSession();
    }

    public static void testConnection() {
        try (Session session = getSession()) {
            session.createNativeQuery("SELECT 1").getResultList();
            logger.info("Database connection test successful");
        } catch (Exception e) {
            logger.error("Database connection test failed: " + e.getMessage(), e);
            throw new RuntimeException("Database connection test failed", e);
        }
    }
} 