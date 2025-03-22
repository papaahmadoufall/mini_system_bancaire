package com.bank.controller;

import java.io.IOException;
import java.net.URL;

import com.bank.dao.ClientDAO;
import com.bank.model.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    
    private ClientDAO clientDAO;
    
    @FXML
    public void initialize() {
        clientDAO = new ClientDAO();
    }
    
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return;
        }
        
        Client client = clientDAO.findByEmail(email);
        if (client != null && client.getMotDePasse().equals(password)) {
            try {
                // Get the URL of the FXML file
                URL dashboardUrl = getClass().getResource("/com/bank/view/dashboard.fxml");
                if (dashboardUrl == null) {
                    throw new IOException("Cannot find dashboard.fxml");
                }
                
                // Create a new FXMLLoader with explicit URL
                FXMLLoader loader = new FXMLLoader(dashboardUrl);
                
                // Load the FXML file
                Parent root = loader.load();
                
                // Get the controller and set the client
                DashboardController dashboardController = loader.getController();
                dashboardController.setClient(client);
                
                // Create a new stage for the dashboard
                Stage dashboardStage = new Stage();
                dashboardStage.setTitle("Mini System Bancaire - Dashboard");
                dashboardStage.setScene(new Scene(root));
                dashboardController.setStage(dashboardStage);
                
                // Close the login stage and show the dashboard
                Stage currentStage = (Stage) emailField.getScene().getWindow();
                currentStage.close();
                dashboardStage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Print the full stack trace for debugging
                showAlert("Erreur", "Erreur lors du chargement du dashboard: " + e.getMessage());
            }
        } else {
            showAlert("Erreur", "Email ou mot de passe incorrect");
        }
    }

    @FXML
    private void handleRegister() {
        try {
            URL registerUrl = getClass().getResource("/fxml/register.fxml");
            if (registerUrl == null) {
                throw new IOException("Cannot find register.fxml");
            }
            Parent root = FXMLLoader.load(registerUrl);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Print the full stack trace for debugging
            showAlert("Erreur", "Erreur lors de la navigation: " + e.getMessage());
        }
    }

    @FXML
    private void handleAdminLogin() {
        try {
            URL location = getClass().getResource("/fxml/admin_login.fxml");
            if (location == null) {
                showAlert("Erreur", "Impossible de trouver le fichier admin_login.fxml");
                return;
            }
            Parent root = FXMLLoader.load(location);
            Scene scene = new Scene(root);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page de connexion administrateur");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 