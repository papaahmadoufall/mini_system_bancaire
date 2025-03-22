package com.bank.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cartes_bancaires")
public class CarteBancaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(unique = true, nullable = false)
    private String numero;

    @Column(nullable = false)
    private LocalDate dateExpiration;

    @Column(nullable = false)
    private double limiteJournaliere;

    @Column(nullable = false)
    private double solde;

    @Column(nullable = false)
    private String statut;

    @Column(nullable = false)
    private String type;

    @OneToMany(mappedBy = "carte", cascade = CascadeType.ALL)
    private List<BankTransaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "carte", cascade = CascadeType.ALL)
    private List<RechargeAutomatique> rechargesAutomatiques = new ArrayList<>();

    public CarteBancaire(Client client, String numero, LocalDate dateExpiration, double limiteJournaliere,
                        double solde, String statut, String type) {
        this.client = client;
        this.numero = numero;
        this.dateExpiration = dateExpiration;
        this.limiteJournaliere = limiteJournaliere;
        this.solde = solde;
        this.statut = statut;
        this.type = type;
    }

    @Override
    public String toString() {
        return "CarteBancaire{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", dateExpiration=" + dateExpiration +
                ", solde=" + solde +
                ", statut='" + statut + '\'' +
                ", limiteJournaliere=" + limiteJournaliere +
                ", client=" + (client != null ? client.getId() : "null") +
                '}';
    }
} 