package com.bank.util;

import java.time.LocalDate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bank.model.Client;

public class DatabaseSeeder {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    public static void seedDatabase() {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                // Check if database is already seeded
                long clientCount = session.createQuery("select count(c) from Client c", Long.class)
                    .getSingleResult();

                if (clientCount == 0) {
                    // Create sample clients
                    Client client1 = new Client();
                    client1.setNom("Doe");
                    client1.setPrenom("John");
                    client1.setEmail("john.doe@example.com");
                    client1.setMotDePasse("password123");
                    client1.setDateNaissance(LocalDate.of(1990, 1, 15));
                    client1.setAdresse("123 Main St");
                    client1.setTelephone("0612345678");
                    client1.setSolde(1000.0);
                    client1.setDateInscription(LocalDate.now());

                    Client client2 = new Client();
                    client2.setNom("Smith");
                    client2.setPrenom("Jane");
                    client2.setEmail("jane.smith@example.com");
                    client2.setMotDePasse("password456");
                    client2.setDateNaissance(LocalDate.of(1992, 5, 20));
                    client2.setAdresse("456 Oak Ave");
                    client2.setTelephone("0623456789");
                    client2.setSolde(2000.0);
                    client2.setDateInscription(LocalDate.now());

                    Client client3 = new Client();
                    client3.setNom("Brown");
                    client3.setPrenom("Mike");
                    client3.setEmail("mike.brown@example.com");
                    client3.setMotDePasse("password789");
                    client3.setDateNaissance(LocalDate.of(1988, 8, 10));
                    client3.setAdresse("789 Pine St");
                    client3.setTelephone("0634567890");
                    client3.setSolde(1500.0);
                    client3.setDateInscription(LocalDate.now());

                    // Save clients to database
                    session.persist(client1);
                    session.persist(client2);
                    session.persist(client3);

                    transaction.commit();
                    logger.info("Database seeded successfully with sample clients");
                } else {
                    logger.info("Database already contains data, skipping seeding");
                }
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Error seeding database: " + e.getMessage(), e);
                throw new RuntimeException("Failed to seed database", e);
            }
        }
    }
} 