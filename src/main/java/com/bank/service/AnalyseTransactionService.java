package com.bank.service;

import java.time.LocalDate;
import java.util.List;

import com.bank.dao.TransactionDAO;
import com.bank.model.BankTransaction;

public class AnalyseTransactionService {
    private final TransactionDAO transactionDAO;

    public AnalyseTransactionService() {
        this.transactionDAO = new TransactionDAO();
    }

    public List<BankTransaction> analyserTransactionsSuspectes(double seuil) {
        return transactionDAO.findTransactionsSuspectes(seuil);
    }

    public void bloquerTransaction(Long transactionId) {
        BankTransaction transaction = transactionDAO.findById(transactionId);
        if (transaction != null) {
            transaction.setStatut("BLOQUÉE");
            transactionDAO.update(transaction);
        }
    }

    public void autoriserTransaction(Long transactionId) {
        BankTransaction transaction = transactionDAO.findById(transactionId);
        if (transaction != null) {
            transaction.setStatut("AUTORISÉE");
            transactionDAO.update(transaction);
        }
    }

    public boolean estTransactionSuspecte(BankTransaction transaction, double seuil) {
        // Vérifier si le montant dépasse le seuil
        if (transaction.getMontant() > seuil) {
            return true;
        }

        // Vérifier si la transaction est effectuée dans un pays différent
        // (à implémenter si vous ajoutez un champ pays dans le modèle BankTransaction)
        
        // Vérifier si la transaction est effectuée à une heure inhabituelle
        // (à implémenter si vous ajoutez un champ heure dans le modèle BankTransaction)
        
        // Vérifier si plusieurs transactions sont effectuées en peu de temps
        // (à implémenter en ajoutant une méthode pour vérifier l'historique des transactions)

        return false;
    }

    public void analyserTransactionsJournalieres() {
        LocalDate aujourdhui = LocalDate.now();
        List<BankTransaction> transactions = transactionDAO.findByDate(aujourdhui);
        
        for (BankTransaction transaction : transactions) {
            if (estTransactionSuspecte(transaction, 1000.0)) { // Seuil par défaut
                transaction.setStatut("SUSPECTE");
                transactionDAO.update(transaction);
            }
        }
    }
} 