package com.bank.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "cartes_bancaires")
public class CarteBancaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private String cvv;

    @Column(name = "date_expiration", nullable = false)
    private LocalDate dateExpiration;

    @Column(nullable = false)
    private Double solde;

    @Column(nullable = false)
    private String statut;

    @Column(name = "limite_journaliere", nullable = false)
    private Double limiteJournaliere;

    @Column(nullable = false)
    private Double cashback;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "carte", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "carte", cascade = CascadeType.ALL)
    private List<Abonnement> abonnements;
} 