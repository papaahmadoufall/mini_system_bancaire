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
@Table(name = "authentifications_transactions")
public class AuthentificationTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private BankTransaction transaction;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime dateEnvoi;

    @Column
    private LocalDateTime dateValidation;

    @Column(nullable = false)
    private String statut;

    public void valider() {
        this.statut = "VALIDÉ";
        this.dateValidation = LocalDateTime.now();
    }
} 