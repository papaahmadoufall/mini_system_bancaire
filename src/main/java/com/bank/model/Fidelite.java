package com.bank.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "fidelites")
public class Fidelite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private int pointsAcquis;

    @Column
    private LocalDateTime dateDernierCredit;

    // Constructors
    public Fidelite() {}

    public Fidelite(Client client, int pointsAcquis, LocalDateTime dateDernierCredit) {
        this.client = client;
        this.pointsAcquis = pointsAcquis;
        this.dateDernierCredit = dateDernierCredit;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getPointsAcquis() {
        return pointsAcquis;
    }

    public void setPointsAcquis(int pointsAcquis) {
        this.pointsAcquis = pointsAcquis;
    }

    public LocalDateTime getDateDernierCredit() {
        return dateDernierCredit;
    }

    public void setDateDernierCredit(LocalDateTime dateDernierCredit) {
        this.dateDernierCredit = dateDernierCredit;
    }
} 