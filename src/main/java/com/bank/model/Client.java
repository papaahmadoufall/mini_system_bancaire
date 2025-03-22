package com.bank.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    @Column(nullable = false)
    private LocalDate dateNaissance;

    @Column(nullable = false)
    private LocalDate dateInscription;

    @Column
    private String adresse;

    @Column
    private String telephone;

    @Column(nullable = false)
    private double solde;

    // Relationships
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<CarteBancaire> cartes;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private Fidelite fidelite;

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", cartes=" + (cartes != null ? cartes.size() + " cartes" : "null") +
                '}';
    }
} 