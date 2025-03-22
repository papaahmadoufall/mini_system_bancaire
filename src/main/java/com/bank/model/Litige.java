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
@Table(name = "litiges")
public class Litige {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private BankTransaction transaction;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String statut;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column
    private LocalDateTime dateResolution;

    @Column
    private String commentaireResolution;

    public void resoudre(String commentaire) {
        this.statut = "RÉSOLU";
        this.dateResolution = LocalDateTime.now();
        this.commentaireResolution = commentaire;
    }
} 