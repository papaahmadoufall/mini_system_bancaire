package com.bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bank.util.DatabaseSeeder;
import com.bank.util.DatabaseUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database connection
            DatabaseUtil.initialize();
            DatabaseUtil.testConnection();
            
            // Seed the database with sample data
            DatabaseSeeder.seedDatabase();

            // Load the login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            primaryStage.setTitle("Banking System - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            logger.error("Error starting application: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 