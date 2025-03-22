package com.bank.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // paiement/retrait

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "carte_id", nullable = false)
    private CarteBancaire carte;

    @Column
    private String commercant;

    @Column(nullable = false)
    private String statut; // validé/rejeté

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private AuthentificationTransaction authentification;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Litige litige;
} 