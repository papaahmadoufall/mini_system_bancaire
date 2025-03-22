package com.bank;

import com.bank.util.DatabaseSeeder;
import com.bank.util.DatabaseUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database
            DatabaseUtil.initialize();

            // Seed database with sample data
            DatabaseSeeder seeder = new DatabaseSeeder();
            seeder.seed();

            // Load and display login view
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Système Bancaire");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 