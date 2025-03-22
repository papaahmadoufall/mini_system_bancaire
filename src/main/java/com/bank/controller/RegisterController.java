package com.bank.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.bank.dao.ClientDAO;
import com.bank.model.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private TextField adresseField;
    @FXML private TextField telephoneField;
    @FXML private TextField soldeField;

    private final ClientDAO clientDAO = new ClientDAO();

    @FXML
    private void handleRegister() {
        if (!validateFields()) {
            return;
        }

        try {
            Client client = new Client();
            client.setNom(nomField.getText().trim());
            client.setPrenom(prenomField.getText().trim());
            client.setEmail(emailField.getText().trim());
            client.setMotDePasse(passwordField.getText());
            client.setDateNaissance(dateNaissancePicker.getValue());
            client.setAdresse(adresseField.getText().trim());
            client.setTelephone(telephoneField.getText().trim());
            client.setSolde(Double.parseDouble(soldeField.getText().trim()));
            client.setDateCreation(LocalDate.now());

            // Check if email already exists
            if (clientDAO.findByEmail(client.getEmail()) != null) {
                showAlert("Erreur", "Un compte existe déjà avec cet email");
                return;
            }

            // Save the new client
            clientDAO.save(client);
            showAlert("Succès", "Compte créé avec succès");
            navigateToLogin();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant du dépôt initial doit être un nombre valide");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'inscription: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        navigateToLogin();
    }

    private boolean validateFields() {
        if (nomField.getText().trim().isEmpty() ||
            prenomField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            passwordField.getText().isEmpty() ||
            dateNaissancePicker.getValue() == null ||
            adresseField.getText().trim().isEmpty() ||
            telephoneField.getText().trim().isEmpty() ||
            soldeField.getText().trim().isEmpty()) {
            
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return false;
        }

        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Erreur", "Format d'email invalide");
            return false;
        }

        if (passwordField.getText().length() < 6) {
            showAlert("Erreur", "Le mot de passe doit contenir au moins 6 caractères");
            return false;
        }

        try {
            double solde = Double.parseDouble(soldeField.getText().trim());
            if (solde < 0) {
                showAlert("Erreur", "Le dépôt initial ne peut pas être négatif");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant du dépôt initial doit être un nombre valide");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de la navigation: " + e.getMessage());
        }
    }
} 