package com.bank.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bank.dao.AdminDAO;
import com.bank.dao.ClientDAO;
import com.bank.dao.FideliteDAO;
import com.bank.dao.CarteBancaireDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Admin;
import com.bank.model.Client;
import com.bank.model.Fidelite;
import com.bank.model.CarteBancaire;
import com.bank.model.BankTransaction;

public class DatabaseSeeder {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
    private final ClientDAO clientDAO;
    private final FideliteDAO fideliteDAO;
    private final AdminDAO adminDAO;
    private final CarteBancaireDAO carteBancaireDAO;
    private final TransactionDAO transactionDAO;

    public DatabaseSeeder() {
        this.clientDAO = new ClientDAO();
        this.fideliteDAO = new FideliteDAO();
        this.adminDAO = new AdminDAO();
        this.carteBancaireDAO = new CarteBancaireDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public void seed() {
        try {
            seedClients();
            seedAdmin();
            seedCards();
            seedTransactions();
            logger.info("Database seeded successfully with sample data");
        } catch (Exception e) {
            logger.error("Error seeding database", e);
        }
    }

    private void seedClients() {
        if (clientDAO.count() == 0) {
            Client client1 = new Client();
            client1.setNom("Dupont");
            client1.setPrenom("Jean");
            client1.setEmail("jean.dupont@email.com");
            client1.setMotDePasse("password123");
            client1.setDateNaissance(LocalDate.of(1990, 1, 1));
            client1.setDateInscription(LocalDate.now());
            client1.setAdresse("123 Rue de Paris");
            client1.setTelephone("0123456789");
            client1.setSolde(1000.0);
            clientDAO.save(client1);

            Fidelite fidelite1 = new Fidelite();
            fidelite1.setClient(client1);
            fidelite1.setPointsAcquis(0);
            fidelite1.setDateDernierCredit(LocalDateTime.now());
            fideliteDAO.save(fidelite1);

            Client client2 = new Client();
            client2.setNom("Martin");
            client2.setPrenom("Marie");
            client2.setEmail("marie.martin@email.com");
            client2.setMotDePasse("password456");
            client2.setDateNaissance(LocalDate.of(1985, 6, 15));
            client2.setDateInscription(LocalDate.now());
            client2.setAdresse("456 Avenue des Champs");
            client2.setTelephone("0987654321");
            client2.setSolde(2000.0);
            clientDAO.save(client2);

            Fidelite fidelite2 = new Fidelite();
            fidelite2.setClient(client2);
            fidelite2.setPointsAcquis(0);
            fidelite2.setDateDernierCredit(LocalDateTime.now());
            fideliteDAO.save(fidelite2);

            Client client3 = new Client();
            client3.setNom("Bernard");
            client3.setPrenom("Sophie");
            client3.setEmail("sophie.bernard@email.com");
            client3.setMotDePasse("password789");
            client3.setDateNaissance(LocalDate.of(1995, 12, 31));
            client3.setDateInscription(LocalDate.now());
            client3.setAdresse("789 Boulevard Central");
            client3.setTelephone("0654321987");
            client3.setSolde(1500.0);
            clientDAO.save(client3);

            Fidelite fidelite3 = new Fidelite();
            fidelite3.setClient(client3);
            fidelite3.setPointsAcquis(0);
            fidelite3.setDateDernierCredit(LocalDateTime.now());
            fideliteDAO.save(fidelite3);
        }
    }

    private void seedAdmin() {
        if (adminDAO.findByUsername("admin") == null) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setRole("ADMIN");
            adminDAO.save(admin);
            logger.info("Default admin user created");
        }
    }

    private void seedCards() {
        if (carteBancaireDAO.count() == 0) {
            // Create cards for each client
            for (Client client : clientDAO.findAll()) {
                CarteBancaire carte = new CarteBancaire();
                carte.setClient(client);
                carte.setNumero(generateCardNumber());
                carte.setDateExpiration(LocalDate.now().plusYears(3));
                carte.setLimiteJournaliere(1000.0);
                carte.setSolde(client.getSolde());
                carte.setStatut("ACTIVE");
                carte.setType("CLASSIC");
                carteBancaireDAO.save(carte);

                // Create a second card for some clients
                if (client.getEmail().equals("jean.dupont@email.com")) {
                    CarteBancaire carte2 = new CarteBancaire();
                    carte2.setClient(client);
                    carte2.setNumero(generateCardNumber());
                    carte2.setDateExpiration(LocalDate.now().plusYears(3));
                    carte2.setLimiteJournaliere(2000.0);
                    carte2.setSolde(client.getSolde());
                    carte2.setStatut("ACTIVE");
                    carte2.setType("GOLD");
                    carteBancaireDAO.save(carte2);
                }
            }
        }
    }

    private void seedTransactions() {
        if (transactionDAO.count() == 0) {
            for (CarteBancaire carte : carteBancaireDAO.findAll()) {
                // Create some sample transactions
                BankTransaction transaction1 = new BankTransaction();
                transaction1.setCarte(carte);
                transaction1.setDate(LocalDate.now());
                transaction1.setMontant(50.0);
                transaction1.setCommercant("Carrefour");
                transaction1.setStatut("COMPLETEE");
                transaction1.setType("PAIEMENT");
                transactionDAO.save(transaction1);

                BankTransaction transaction2 = new BankTransaction();
                transaction2.setCarte(carte);
                transaction2.setDate(LocalDate.now().minusDays(1));
                transaction2.setMontant(100.0);
                transaction2.setCommercant("Fnac");
                transaction2.setStatut("COMPLETEE");
                transaction2.setType("PAIEMENT");
                transactionDAO.save(transaction2);
            }
        }
    }

    private String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
} 