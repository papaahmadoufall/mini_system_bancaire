package com.bank.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.bank.dao.ClientDAO;
import com.bank.dao.CarteBancaireDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Client;
import com.bank.model.CarteBancaire;
import com.bank.model.BankTransaction;
import com.bank.util.SessionManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ClientDashboardController {
    @FXML private Label balanceLabel;
    @FXML private TableView<CarteBancaire> cardsTable;
    @FXML private TableView<BankTransaction> transactionsTable;

    private ClientDAO clientDAO;
    private CarteBancaireDAO carteBancaireDAO;
    private TransactionDAO transactionDAO;
    private Client currentClient;

    @FXML
    public void initialize() {
        clientDAO = new ClientDAO();
        carteBancaireDAO = new CarteBancaireDAO();
        transactionDAO = new TransactionDAO();

        // Get current client from session
        currentClient = SessionManager.getCurrentClient();
        if (currentClient == null) {
            handleLogout();
            return;
        }

        setupTables();
        refreshBalance();
        loadCards();
        loadTransactions();
    }

    private void setupTables() {
        // Setup cards table
        cardsTable.getColumns().forEach(column -> {
            String propertyName = column.getId().replace("Column", "");
            column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        });

        // Setup transactions table
        transactionsTable.getColumns().forEach(column -> {
            String propertyName = column.getId().replace("Column", "");
            column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        });
    }

    @FXML
    private void refreshBalance() {
        if (currentClient != null) {
            double balance = currentClient.getSolde();
            balanceLabel.setText(String.format("%.2f €", balance));
        }
    }

    private void loadCards() {
        List<CarteBancaire> cards = carteBancaireDAO.findByClient(currentClient);
        cardsTable.getItems().setAll(cards);
    }

    private void loadTransactions() {
        List<BankTransaction> transactions = transactionDAO.findByClient(currentClient);
        transactionsTable.getItems().setAll(transactions);
    }

    @FXML
    private void handleLogout() {
        try {
            SessionManager.clearSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bank/view/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) balanceLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 