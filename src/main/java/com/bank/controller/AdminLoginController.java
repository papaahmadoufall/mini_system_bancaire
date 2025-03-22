package com.bank.controller;

import java.io.IOException;
import java.net.URL;

import com.bank.dao.AdminDAO;
import com.bank.model.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private AdminDAO adminDAO;

    @FXML
    public void initialize() {
        adminDAO = new AdminDAO();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return;
        }

        Admin admin = adminDAO.findByUsername(username);
        if (admin == null || !admin.getPassword().equals(password)) {
            showAlert("Erreur", "Nom d'utilisateur ou mot de passe incorrect");
            return;
        }

        try {
            URL location = getClass().getResource("/fxml/admin_dashboard.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            AdminDashboardController controller = loader.getController();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement du tableau de bord administrateur");
        }
    }

    @FXML
    private void handleBack() {
        try {
            URL location = getClass().getResource("/fxml/login.fxml");
            Parent root = FXMLLoader.load(location);
            Scene scene = new Scene(root);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page de connexion");
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