package com.bank.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.bank.dao.CarteBancaireDAO;
import com.bank.dao.ClientDAO;
import com.bank.dao.RechargeAutomatiqueDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.BankTransaction;
import com.bank.model.CarteBancaire;
import com.bank.model.Client;
import com.bank.model.RechargeAutomatique;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminDashboardController {
    @FXML private TextField seuilSuspicionField;
    @FXML private TableView<BankTransaction> transactionsSuspectesTable;
    @FXML private TableColumn<BankTransaction, String> clientColumn;
    @FXML private TableColumn<BankTransaction, String> carteColumn;
    @FXML private TableColumn<BankTransaction, Double> montantColumn;
    @FXML private TableColumn<BankTransaction, LocalDate> dateColumn;
    @FXML private TableColumn<BankTransaction, String> commercantColumn;
    @FXML private TableColumn<BankTransaction, String> typeColumn;
    @FXML private TableColumn<BankTransaction, String> statutColumn;

    @FXML private ComboBox<Client> clientComboBox;
    @FXML private ComboBox<String> typeCarteComboBox;
    @FXML private TextField limiteJournaliereField;
    @FXML private TextField montantInitialField;
    @FXML private TableView<CarteBancaire> cartesTable;
    @FXML private TableColumn<CarteBancaire, String> numeroCarteColumn;
    @FXML private TableColumn<CarteBancaire, String> clientCarteColumn;
    @FXML private TableColumn<CarteBancaire, String> typeCarteColumn;
    @FXML private TableColumn<CarteBancaire, Double> soldeCarteColumn;
    @FXML private TableColumn<CarteBancaire, Double> limiteCarteColumn;
    @FXML private TableColumn<CarteBancaire, String> statutCarteColumn;

    @FXML private ComboBox<CarteBancaire> carteRechargeComboBox;
    @FXML private TextField seuilRechargeField;
    @FXML private TextField montantRechargeField;
    @FXML private TableView<RechargeAutomatique> rechargesTable;
    @FXML private TableColumn<RechargeAutomatique, String> carteRechargeColumn;
    @FXML private TableColumn<RechargeAutomatique, Double> seuilRechargeColumn;
    @FXML private TableColumn<RechargeAutomatique, Double> montantRechargeColumn;
    @FXML private TableColumn<RechargeAutomatique, String> statutRechargeColumn;
    @FXML private TableColumn<RechargeAutomatique, LocalDateTime> derniereRechargeColumn;

    @FXML private ComboBox<Client> clientRechargeComboBox;
    @FXML private TextField montantAjoutField;
    @FXML private TableView<Client> clientsTable;
    @FXML private TableColumn<Client, Long> clientIdColumn;
    @FXML private TableColumn<Client, String> clientNomColumn;
    @FXML private TableColumn<Client, String> clientPrenomColumn;
    @FXML private TableColumn<Client, String> clientEmailColumn;
    @FXML private TableColumn<Client, Double> clientSoldeColumn;
    @FXML private TableColumn<Client, String> clientTelephoneColumn;
    @FXML private TableView<Client> clientTable;

    @FXML private Label soldeClientLabel;

    private TransactionDAO transactionDAO;
    private ClientDAO clientDAO;
    private CarteBancaireDAO carteBancaireDAO;
    private RechargeAutomatiqueDAO rechargeAutomatiqueDAO;
    private Stage stage;
    private ObservableList<Client> clientList;

    @FXML
    public void initialize() {
        transactionDAO = new TransactionDAO();
        clientDAO = new ClientDAO();
        carteBancaireDAO = new CarteBancaireDAO();
        rechargeAutomatiqueDAO = new RechargeAutomatiqueDAO();

        // Configure client combo box listener
        clientComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                soldeClientLabel.setText(String.format("%.2f €", newVal.getSolde()));
            } else {
                soldeClientLabel.setText("");
            }
        });

        // Configure transaction columns
        clientColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getCarte().getClient().getPrenom() + " " + 
                                     cellData.getValue().getCarte().getClient().getNom()));
        carteColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getCarte().getNumero()));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        commercantColumn.setCellValueFactory(new PropertyValueFactory<>("commercant"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Format montant column
        montantColumn.setCellFactory(column -> new TableCell<BankTransaction, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                if (empty || montant == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", montant));
                }
            }
        });

        // Format date column
        dateColumn.setCellFactory(column -> new TableCell<BankTransaction, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });

        // Configure carte columns
        numeroCarteColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        clientCarteColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getClient().getPrenom() + " " + 
                                     cellData.getValue().getClient().getNom()));
        typeCarteColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        soldeCarteColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        limiteCarteColumn.setCellValueFactory(new PropertyValueFactory<>("limiteJournaliere"));
        statutCarteColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Format solde and limite columns
        soldeCarteColumn.setCellFactory(column -> new TableCell<CarteBancaire, Double>() {
            @Override
            protected void updateItem(Double solde, boolean empty) {
                super.updateItem(solde, empty);
                if (empty || solde == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", solde));
                }
            }
        });

        limiteCarteColumn.setCellFactory(column -> new TableCell<CarteBancaire, Double>() {
            @Override
            protected void updateItem(Double limite, boolean empty) {
                super.updateItem(limite, empty);
                if (empty || limite == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", limite));
                }
            }
        });

        // Configure recharge columns
        carteRechargeColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getCarte().getNumero()));
        seuilRechargeColumn.setCellValueFactory(new PropertyValueFactory<>("seuil"));
        montantRechargeColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        statutRechargeColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        derniereRechargeColumn.setCellValueFactory(new PropertyValueFactory<>("derniereRecharge"));

        // Format recharge columns
        seuilRechargeColumn.setCellFactory(column -> new TableCell<RechargeAutomatique, Double>() {
            @Override
            protected void updateItem(Double seuil, boolean empty) {
                super.updateItem(seuil, empty);
                if (empty || seuil == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", seuil));
                }
            }
        });

        montantRechargeColumn.setCellFactory(column -> new TableCell<RechargeAutomatique, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                if (empty || montant == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", montant));
                }
            }
        });

        // Initialize type carte combo box
        typeCarteComboBox.setItems(FXCollections.observableArrayList("Classique", "Premium", "Business"));

        // Configure client table columns
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientNomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        clientPrenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        clientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientSoldeColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        clientTelephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        // Format solde column
        clientSoldeColumn.setCellFactory(column -> new TableCell<Client, Double>() {
            @Override
            protected void updateItem(Double solde, boolean empty) {
                super.updateItem(solde, empty);
                if (empty || solde == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", solde));
                }
            }
        });

        refreshData();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void refreshData() {
        // Load clients for combo box
        List<Client> clients = clientDAO.findAll();
        clientComboBox.setItems(FXCollections.observableArrayList(clients));
        clientRechargeComboBox.setItems(FXCollections.observableArrayList(clients));
        clientsTable.setItems(FXCollections.observableArrayList(clients));

        // Load cartes for recharge combo box
        List<CarteBancaire> cartes = carteBancaireDAO.findAll();
        carteRechargeComboBox.setItems(FXCollections.observableArrayList(cartes));

        // Load transactions suspectes with default seuil if field is empty
        double seuil = 1000.0; // Default value
        try {
            String seuilText = seuilSuspicionField.getText();
            if (seuilText != null && !seuilText.trim().isEmpty()) {
                seuil = Double.parseDouble(seuilText);
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Valeur invalide");
            alert.setHeaderText(null);
            alert.setContentText("Le seuil de suspicion doit être un nombre valide. Utilisation de la valeur par défaut (1000€).");
            alert.showAndWait();
        }
        List<BankTransaction> transactionsSuspectes = transactionDAO.findTransactionsSuspectes(seuil);
        transactionsSuspectesTable.setItems(FXCollections.observableArrayList(transactionsSuspectes));

        // Load cartes
        cartesTable.setItems(FXCollections.observableArrayList(cartes));

        // Load recharges automatiques
        List<RechargeAutomatique> recharges = rechargeAutomatiqueDAO.findAll();
        rechargesTable.setItems(FXCollections.observableArrayList(recharges));
    }

    @FXML
    private void handleActualiserSuspicion() {
        refreshData();
    }

    @FXML
    private void handleBloquerTransaction() {
        BankTransaction transaction = transactionsSuspectesTable.getSelectionModel().getSelectedItem();
        if (transaction != null) {
            transaction.setStatut("BLOQUÉE");
            transactionDAO.update(transaction);
            refreshData();
            showAlert("Succès", "Transaction bloquée avec succès");
        } else {
            showAlert("Erreur", "Veuillez sélectionner une transaction");
        }
    }

    @FXML
    private void handleAutoriserTransaction() {
        BankTransaction transaction = transactionsSuspectesTable.getSelectionModel().getSelectedItem();
        if (transaction != null) {
            transaction.setStatut("AUTORISÉE");
            transactionDAO.update(transaction);
            refreshData();
            showAlert("Succès", "Transaction autorisée avec succès");
        } else {
            showAlert("Erreur", "Veuillez sélectionner une transaction");
        }
    }

    @FXML
    private void handleCreerCarte() {
        try {
            Client client = clientComboBox.getValue();
            String type = typeCarteComboBox.getValue();
            String limiteText = limiteJournaliereField.getText().trim();
            String montantInitialText = montantInitialField.getText().trim();

            // Validate inputs
            if (client == null) {
                showAlert("Erreur", "Veuillez sélectionner un client");
                return;
            }
            if (type == null) {
                showAlert("Erreur", "Veuillez sélectionner un type de carte");
                return;
            }
            if (limiteText.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer une limite journalière");
                return;
            }
            if (montantInitialText.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un montant initial");
                return;
            }

            double limite = Double.parseDouble(limiteText);
            double montantInitial = Double.parseDouble(montantInitialText);

            if (limite <= 0) {
                showAlert("Erreur", "La limite journalière doit être supérieure à 0");
                return;
            }
            if (montantInitial < 0) {
                showAlert("Erreur", "Le montant initial ne peut pas être négatif");
                return;
            }

            // Vérifier si le client a suffisamment de solde
            if (montantInitial > client.getSolde()) {
                showAlert("Erreur", "Le montant initial ne peut pas dépasser le solde du compte client");
                return;
            }

            // Create new card
            CarteBancaire carte = new CarteBancaire();
            carte.setClient(client);
            carte.setType(type);
            carte.setLimiteJournaliere(limite);
            carte.setSolde(montantInitial);
            carte.setStatut("ACTIVE");
            carte.setDateExpiration(LocalDate.now().plusYears(4));
            carte.setNumero(generateCardNumber());

            // Mettre à jour le solde du client
            client.setSolde(client.getSolde() - montantInitial);

            // Save the card and update client
            carteBancaireDAO.save(carte);
            clientDAO.update(client);

            // Create initial transaction if montantInitial > 0
            if (montantInitial > 0) {
                BankTransaction transaction = new BankTransaction();
                transaction.setCarte(carte);
                transaction.setMontant(montantInitial);
                transaction.setCommercant("ADMIN");
                transaction.setDate(LocalDate.now());
                transaction.setType("DÉPÔT INITIAL");
                transaction.setStatut("VALIDÉE");
                transactionDAO.save(transaction);
            }
            
            // Clear the form
            limiteJournaliereField.clear();
            montantInitialField.clear();
            
            // Refresh the data
            refreshData();
            
            showAlert("Succès", "Carte créée avec succès");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les montants doivent être des nombres valides");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la création de la carte");
        }
    }

    @FXML
    private void handleSuspendreCarte() {
        CarteBancaire carte = cartesTable.getSelectionModel().getSelectedItem();
        if (carte != null) {
            carte.setStatut("SUSPENDUE");
            carteBancaireDAO.update(carte);
            refreshData();
            showAlert("Succès", "Carte suspendue avec succès");
        } else {
            showAlert("Erreur", "Veuillez sélectionner une carte");
        }
    }

    @FXML
    private void handleActiverCarte() {
        CarteBancaire carte = cartesTable.getSelectionModel().getSelectedItem();
        if (carte != null) {
            carte.setStatut("ACTIVE");
            carteBancaireDAO.update(carte);
            refreshData();
            showAlert("Succès", "Carte activée avec succès");
        } else {
            showAlert("Erreur", "Veuillez sélectionner une carte");
        }
    }

    @FXML
    private void handleConfigurerRecharge() {
        CarteBancaire carte = carteRechargeComboBox.getValue();
        double seuil = Double.parseDouble(seuilRechargeField.getText());
        double montant = Double.parseDouble(montantRechargeField.getText());

        if (carte != null && seuil > 0 && montant > 0) {
            RechargeAutomatique recharge = new RechargeAutomatique();
            recharge.setCarte(carte);
            recharge.setSeuil(seuil);
            recharge.setMontant(montant);
            recharge.setStatut("ACTIVE");
            recharge.setDerniereRecharge(LocalDateTime.now());

            rechargeAutomatiqueDAO.save(recharge);
            refreshData();
            showAlert("Succès", "Recharge automatique configurée avec succès");
        } else {
            showAlert("Erreur", "Veuillez remplir tous les champs correctement");
        }
    }

    @FXML
    private void handleActiverRecharge() {
        RechargeAutomatique recharge = rechargesTable.getSelectionModel().getSelectedItem();
        if (recharge != null) {
            recharge.setStatut("ACTIVE");
            rechargeAutomatiqueDAO.update(recharge);
            refreshData();
            showAlert("Succès", "Recharge automatique activée avec succès");
        } else {
            showAlert("Erreur", "Veuillez sélectionner une recharge automatique");
        }
    }

    @FXML
    private void handleDesactiverRecharge() {
        RechargeAutomatique recharge = rechargesTable.getSelectionModel().getSelectedItem();
        if (recharge != null) {
            recharge.setStatut("INACTIVE");
            rechargeAutomatiqueDAO.update(recharge);
            refreshData();
            showAlert("Succès", "Recharge automatique désactivée avec succès");
        } else {
            showAlert("Erreur", "Veuillez sélectionner une recharge automatique");
        }
    }

    @FXML
    private void handleDeconnexion() {
        try {
            URL location = getClass().getResource("/fxml/admin_login.fxml");
            Parent root = FXMLLoader.load(location);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page de connexion administrateur");
        }
    }

    @FXML
    private void handleAjouterArgent() {
        Client client = clientRechargeComboBox.getValue();
        if (client == null) {
            showAlert("Erreur", "Veuillez sélectionner un client");
            return;
        }

        try {
            double montant = Double.parseDouble(montantAjoutField.getText());
            if (montant <= 0) {
                showAlert("Erreur", "Le montant doit être supérieur à 0");
                return;
            }

            // Update client balance
            client.setSolde(client.getSolde() + montant);
            clientDAO.update(client);

            // Create a transaction record
            if (!client.getCartes().isEmpty()) {
                CarteBancaire carte = client.getCartes().get(0);
                BankTransaction transaction = new BankTransaction();
                transaction.setCarte(carte);
                transaction.setMontant(montant);
                transaction.setCommercant("ADMIN");
                transaction.setDate(LocalDate.now());
                transaction.setType("RECHARGE");
                transaction.setStatut("VALIDÉE");
                transactionDAO.save(transaction);
            }

            refreshData();
            montantAjoutField.clear();
            showAlert("Succès", String.format("%.2f € ont été ajoutés au compte de %s %s", 
                     montant, client.getPrenom(), client.getNom()));
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un montant valide");
        }
    }

    @FXML
    private void handleRefresh() {
        refreshData();
    }

    @FXML
    private void handleLogout() {
        try {
            URL location = getClass().getResource("/fxml/login.fxml");
            Parent root = FXMLLoader.load(location);
            Scene scene = new Scene(root);
            Stage stage = (Stage) clientTable.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page de connexion");
        }
    }

    @FXML
    private void handleBloquerCarte() {
        CarteBancaire selectedCarte = cartesTable.getSelectionModel().getSelectedItem();
        if (selectedCarte == null) {
            showAlert("Erreur", "Veuillez sélectionner une carte à bloquer.");
            return;
        }

        selectedCarte.setStatut("BLOQUEE");
        carteBancaireDAO.update(selectedCarte);
        refreshData();
        showAlert("Succès", "La carte a été bloquée avec succès.");
    }

    @FXML
    private void handleDebloquerCarte() {
        CarteBancaire selectedCarte = cartesTable.getSelectionModel().getSelectedItem();
        if (selectedCarte == null) {
            showAlert("Erreur", "Veuillez sélectionner une carte à débloquer.");
            return;
        }

        selectedCarte.setStatut("ACTIVE");
        carteBancaireDAO.update(selectedCarte);
        refreshData();
        showAlert("Succès", "La carte a été débloquée avec succès.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String generateCardNumber() {
        // Génère un numéro de carte au format 16 chiffres
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append((int)(Math.random() * 10));
        }
        return sb.toString();
    }
} 