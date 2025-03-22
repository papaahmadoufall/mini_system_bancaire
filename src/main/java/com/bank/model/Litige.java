package com.bank.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "litiges")
public class Litige {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(nullable = false)
    private String motif;

    @Column(name = "date_signalement", nullable = false)
    private LocalDateTime dateSignalement;

    @Column(nullable = false)
    private String statut;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;
} 