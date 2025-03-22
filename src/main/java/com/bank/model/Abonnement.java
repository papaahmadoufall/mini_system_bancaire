package com.bank.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "abonnements")
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carte_id", nullable = false)
    private CarteBancaire carte;

    @Column(nullable = false)
    private String service;

    @Column(nullable = false)
    private Double montant;

    @Column(name = "date_prelevement", nullable = false)
    private LocalDate datePrelevement;

    @Column(nullable = false)
    private String statut; // actif/annulé
} 