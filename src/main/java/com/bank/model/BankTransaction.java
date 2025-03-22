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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carte_id", nullable = false)
    private CarteBancaire carte;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String commercant;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String statut;
} 