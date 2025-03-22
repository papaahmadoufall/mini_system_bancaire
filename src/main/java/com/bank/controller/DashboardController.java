package com.bank.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.bank.dao.AbonnementDAO;
import com.bank.dao.AuthentificationTransactionDAO;
import com.bank.dao.CarteBancaireDAO;
import com.bank.dao.ClientDAO;
import com.bank.dao.LitigeDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Abonnement;
import com.bank.model.BankTransaction;
import com.bank.model.CarteBancaire;
import com.bank.model.Client;
import com.bank.model.Litige;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DashboardController {
    @FXML private Label clientNameLabel;
    @FXML private Label pointsFideliteLabel;
    @FXML private Label cashbackLabel;
    @FXML private TableView<CarteBancaire> cartesTable;
    @FXML private TableColumn<CarteBancaire, String> numeroColumn;
    @FXML private TableColumn<CarteBancaire, LocalDate> dateExpirationColumn;
    @FXML private TableColumn<CarteBancaire, Double> soldeColumn;
    @FXML private TableColumn<CarteBancaire, String> statutCarteColumn;
    
    @FXML private ComboBox<CarteBancaire> carteComboBox;
    @FXML private ComboBox<CarteBancaire> carteRetraitComboBox;
    @FXML private TextField montantField;
    @FXML private TextField montantRetraitField;
    @FXML private TextField commercantField;
    
    @FXML private TableView<BankTransaction> transactionTable;
    @FXML private TableColumn<BankTransaction, LocalDate> dateColumn;
    @FXML private TableColumn<BankTransaction, String> typeColumn;
    @FXML private TableColumn<BankTransaction, Double> montantColumn;
    @FXML private TableColumn<BankTransaction, String> commercantColumn;
    @FXML private TableColumn<BankTransaction, String> statutColumn;

    @FXML private TableView<Abonnement> abonnementsTable;
    @FXML private TableColumn<Abonnement, String> serviceColumn;
    @FXML private TableColumn<Abonnement, Double> montantAbonnementColumn;
    @FXML private TableColumn<Abonnement, LocalDate> datePrelevementColumn;
    @FXML private TableColumn<Abonnement, String> statutAbonnementColumn;
    @FXML private ComboBox<CarteBancaire> carteAbonnementComboBox;
    @FXML private TextField serviceField;
    @FXML private TextField montantAbonnementField;

    @FXML private TableView<Litige> litigesTable;
    @FXML private TableColumn<Litige, String> motifColumn;
    @FXML private TableColumn<Litige, String> statutLitigeColumn;
    @FXML private TableColumn<Litige, LocalDateTime> dateCreationColumn;
    @FXML private TableColumn<Litige, LocalDateTime> dateResolutionColumn;

    @FXML private TextArea motifLitigeArea;
    @FXML private Button signalerLitigeButton;

    @FXML private ComboBox<String> typeCarteComboBox;
    @FXML private TextField montantInitialField;
    @FXML private TableColumn<CarteBancaire, Double> limiteColumn;

    private Client client;
    private CarteBancaireDAO carteBancaireDAO;
    private TransactionDAO transactionDAO;
    private AuthentificationTransactionDAO authentificationDAO;
    private AbonnementDAO abonnementDAO;
    private LitigeDAO litigeDAO;
    private ClientDAO clientDAO;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        carteBancaireDAO = new CarteBancaireDAO();
        transactionDAO = new TransactionDAO();
        authentificationDAO = new AuthentificationTransactionDAO();
        abonnementDAO = new AbonnementDAO();
        litigeDAO = new LitigeDAO();
        clientDAO = new ClientDAO();

        // Configure table columns
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        dateExpirationColumn.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        soldeColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        statutCarteColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        limiteColumn.setCellValueFactory(new PropertyValueFactory<>("limiteJournaliere"));

        // Configure transaction table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        commercantColumn.setCellValueFactory(new PropertyValueFactory<>("commercant"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Configure abonnement table columns
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        montantAbonnementColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        datePrelevementColumn.setCellValueFactory(new PropertyValueFactory<>("datePrelevement"));
        statutAbonnementColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Configure litige table columns
        motifColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statutLitigeColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        dateCreationColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        dateResolutionColumn.setCellValueFactory(new PropertyValueFactory<>("dateResolution"));

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

        // Format solde column
        soldeColumn.setCellFactory(column -> new TableCell<CarteBancaire, Double>() {
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

        // Format limite column
        limiteColumn.setCellFactory(column -> new TableCell<CarteBancaire, Double>() {
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

        // Initialize type carte combo box
        typeCarteComboBox.setItems(FXCollections.observableArrayList("Classique", "Premium", "Business"));
    }

    public void setClient(Client client) {
        this.client = client;
        clientNameLabel.setText(client.getNom() + " " + client.getPrenom());
        refreshData();
    }

    private void refreshData() {
        // Refresh cartes table
        List<CarteBancaire> cartes = carteBancaireDAO.findByClient(client);
        cartesTable.setItems(FXCollections.observableArrayList(cartes));

        // Refresh transaction table
        List<BankTransaction> transactions = transactionDAO.findByClient(client);
        transactionTable.setItems(FXCollections.observableArrayList(transactions));

        // Refresh abonnements table
        List<Abonnement> abonnements = abonnementDAO.findByClient(client);
        abonnementsTable.setItems(FXCollections.observableArrayList(abonnements));

        // Refresh litiges table
        List<Litige> litiges = litigeDAO.findByClient(client);
        litigesTable.setItems(FXCollections.observableArrayList(litiges));

        // Update combo boxes
        carteComboBox.setItems(FXCollections.observableArrayList(cartes));
        carteRetraitComboBox.setItems(FXCollections.observableArrayList(cartes));
        carteAbonnementComboBox.setItems(FXCollections.observableArrayList(cartes));
    }

    @FXML
    private void handlePaiement() {
        CarteBancaire carte = carteComboBox.getValue();
        if (carte == null) {
            showAlert("Erreur", "Veuillez sélectionner une carte");
            return;
        }

        try {
            double montant = Double.parseDouble(montantField.getText());
            String commercant = commercantField.getText();

            if (montant <= 0) {
                showAlert("Erreur", "Le montant doit être supérieur à 0");
                return;
            }

            if (carte.getSolde() < montant) {
                showAlert("Erreur", "Solde insuffisant");
                return;
            }

            BankTransaction transaction = new BankTransaction();
            transaction.setCarte(carte);
            transaction.setMontant(montant);
            transaction.setCommercant(commercant);
            transaction.setDate(LocalDate.now());
            transaction.setType("PAIEMENT");
            transaction.setStatut("EN_COURS");

            transactionDAO.save(transaction);
            carte.setSolde(carte.getSolde() - montant);
            carteBancaireDAO.update(carte);

            refreshData();
            montantField.clear();
            commercantField.clear();
            showAlert("Succès", "Paiement effectué avec succès");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un montant valide");
        }
    }

    @FXML
    private void handleRetrait() {
        CarteBancaire carte = carteRetraitComboBox.getValue();
        if (carte == null) {
            showAlert("Erreur", "Veuillez sélectionner une carte");
            return;
        }

        try {
            double montant = Double.parseDouble(montantRetraitField.getText());

            if (montant <= 0) {
                showAlert("Erreur", "Le montant doit être supérieur à 0");
                return;
            }

            if (carte.getSolde() < montant) {
                showAlert("Erreur", "Solde insuffisant");
                return;
            }

            BankTransaction transaction = new BankTransaction();
            transaction.setCarte(carte);
            transaction.setMontant(montant);
            transaction.setCommercant("RETRAIT");
            transaction.setDate(LocalDate.now());
            transaction.setType("RETRAIT");
            transaction.setStatut("EN_COURS");

            transactionDAO.save(transaction);
            carte.setSolde(carte.getSolde() - montant);
            carteBancaireDAO.update(carte);

            refreshData();
            montantRetraitField.clear();
            showAlert("Succès", "Retrait effectué avec succès");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un montant valide");
        }
    }

    @FXML
    private void handleAbonnement() {
        CarteBancaire carte = carteAbonnementComboBox.getValue();
        if (carte == null) {
            showAlert("Erreur", "Veuillez sélectionner une carte");
            return;
        }

        try {
            String service = serviceField.getText();
            double montant = Double.parseDouble(montantAbonnementField.getText());

            if (service == null || service.trim().isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un service");
                return;
            }

            if (montant <= 0) {
                showAlert("Erreur", "Le montant doit être supérieur à 0");
                return;
            }

            Abonnement abonnement = new Abonnement();
            abonnement.setCarte(carte);
            abonnement.setService(service);
            abonnement.setMontant(montant);
            abonnement.setDatePrelevement(LocalDate.now().plusMonths(1));
            abonnement.setStatut("ACTIF");

            abonnementDAO.save(abonnement);
            refreshData();
            serviceField.clear();
            montantAbonnementField.clear();
            showAlert("Succès", "Abonnement créé avec succès");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un montant valide");
        }
    }

    @FXML
    private void handleSignalerLitige() {
        BankTransaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showAlert("Erreur", "Veuillez sélectionner une transaction");
            return;
        }

        String motif = motifLitigeArea.getText().trim();
        if (motif.isEmpty()) {
            showAlert("Erreur", "Veuillez saisir le motif du litige");
            return;
        }

        Litige litige = new Litige();
        litige.setTransaction(selectedTransaction);
        litige.setDescription(motif);
        litige.setStatut("EN_ATTENTE");
        litige.setDateCreation(LocalDateTime.now());

        litigeDAO.save(litige);
        motifLitigeArea.clear();
        refreshData();
        
        showAlert("Succès", "Litige signalé avec succès");
    }

    @FXML
    private void handleLogout() {
        try {
            URL location = getClass().getResource("/com/bank/view/login.fxml");
            if (location == null) {
                location = getClass().getResource("../view/login.fxml");
            }
            if (location == null) {
                location = DashboardController.class.getResource("/com/bank/view/login.fxml");
            }
            if (location == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Impossible de trouver le fichier login.fxml. Chemins essayés:\n" +
                                   "- /com/bank/view/login.fxml\n" +
                                   "- ../view/login.fxml");
                alert.showAndWait();
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) clientNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors du chargement de la page de connexion: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDemanderCarte() {
        try {
            String type = typeCarteComboBox.getValue();
            String montantInitialText = montantInitialField.getText().trim();

            if (type == null) {
                showAlert("Erreur", "Veuillez sélectionner un type de carte");
                return;
            }
            if (montantInitialText.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un montant initial");
                return;
            }

            double montantInitial = Double.parseDouble(montantInitialText);

            if (montantInitial < 0) {
                showAlert("Erreur", "Le montant initial ne peut pas être négatif");
                return;
            }

            // Vérifier si le client a suffisamment de solde
            if (montantInitial > client.getSolde()) {
                showAlert("Erreur", "Le montant initial ne peut pas dépasser votre solde");
                return;
            }

            // Create new card
            CarteBancaire carte = new CarteBancaire();
            carte.setClient(client);
            carte.setType(type);
            carte.setLimiteJournaliere(determinerLimiteJournaliere(type));
            carte.setSolde(montantInitial);
            carte.setStatut("ACTIVE");
            carte.setDateExpiration(LocalDate.now().plusYears(4));
            carte.setNumero(generateCardNumber());

            // Mettre à jour le solde du client
            client.setSolde(client.getSolde() - montantInitial);

            // Save the card and update client
            carteBancaireDAO.save(carte);
            clientDAO.update(client);

            // Create initial transaction
            if (montantInitial > 0) {
                BankTransaction transaction = new BankTransaction();
                transaction.setCarte(carte);
                transaction.setMontant(montantInitial);
                transaction.setCommercant("BANK");
                transaction.setDate(LocalDate.now());
                transaction.setType("DÉPÔT INITIAL");
                transaction.setStatut("VALIDÉE");
                transactionDAO.save(transaction);
            }
            
            // Clear the form
            montantInitialField.clear();
            typeCarteComboBox.setValue(null);
            
            // Refresh the data
            refreshData();
            
            showAlert("Succès", "Carte créée avec succès");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant initial doit être un nombre valide");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la création de la carte");
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
    private void handleSuspendreCard() {
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

    private double determinerLimiteJournaliere(String type) {
        switch (type) {
            case "Classique":
                return 1000.0;
            case "Premium":
                return 5000.0;
            case "Business":
                return 10000.0;
            default:
                return 1000.0;
        }
    }

    private String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append((int)(Math.random() * 10));
        }
        return sb.toString();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 