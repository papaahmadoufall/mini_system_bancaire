package com.bank.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recharges_automatiques")
public class RechargeAutomatique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carte_id", nullable = false)
    private CarteBancaire carte;

    @Column(nullable = false)
    private Double seuil;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private String statut;

    @Column(name = "derniere_recharge", nullable = false)
    private LocalDateTime derniereRecharge;

    public RechargeAutomatique(CarteBancaire carte, Double seuil, Double montant, String statut) {
        this.carte = carte;
        this.seuil = seuil;
        this.montant = montant;
        this.statut = statut;
    }
} 