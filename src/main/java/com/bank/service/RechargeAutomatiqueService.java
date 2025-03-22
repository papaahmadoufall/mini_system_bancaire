package com.bank.service;

import java.time.LocalDateTime;
import java.util.List;

import com.bank.dao.CarteBancaireDAO;
import com.bank.dao.RechargeAutomatiqueDAO;
import com.bank.model.CarteBancaire;
import com.bank.model.RechargeAutomatique;

public class RechargeAutomatiqueService {
    private final RechargeAutomatiqueDAO rechargeAutomatiqueDAO;
    private final CarteBancaireDAO carteBancaireDAO;

    public RechargeAutomatiqueService() {
        this.rechargeAutomatiqueDAO = new RechargeAutomatiqueDAO();
        this.carteBancaireDAO = new CarteBancaireDAO();
    }

    public void verifierEtRecharger() {
        List<RechargeAutomatique> rechargesActives = rechargeAutomatiqueDAO.findActive();
        
        for (RechargeAutomatique recharge : rechargesActives) {
            CarteBancaire carte = recharge.getCarte();
            
            // Vérifier si le solde est inférieur au seuil
            if (carte.getSolde() < recharge.getSeuil()) {
                // Effectuer la recharge
                double nouveauSolde = carte.getSolde() + recharge.getMontant();
                carte.setSolde(nouveauSolde);
                carteBancaireDAO.update(carte);
                
                // Mettre à jour la date de dernière recharge
                recharge.setDerniereRecharge(LocalDateTime.now());
                rechargeAutomatiqueDAO.update(recharge);
            }
        }
    }

    public void configurerRecharge(CarteBancaire carte, double seuil, double montant) {
        // Vérifier si une recharge automatique existe déjà pour cette carte
        List<RechargeAutomatique> rechargesExistentes = rechargeAutomatiqueDAO.findByCarteId(carte.getId());
        
        if (!rechargesExistentes.isEmpty()) {
            // Mettre à jour la recharge existante
            RechargeAutomatique rechargeExistante = rechargesExistentes.get(0);
            rechargeExistante.setSeuil(seuil);
            rechargeExistante.setMontant(montant);
            rechargeExistante.setStatut("ACTIVE");
            rechargeAutomatiqueDAO.update(rechargeExistante);
        } else {
            // Créer une nouvelle recharge automatique
            RechargeAutomatique nouvelleRecharge = new RechargeAutomatique();
            nouvelleRecharge.setCarte(carte);
            nouvelleRecharge.setSeuil(seuil);
            nouvelleRecharge.setMontant(montant);
            nouvelleRecharge.setStatut("ACTIVE");
            nouvelleRecharge.setDerniereRecharge(LocalDateTime.now());
            rechargeAutomatiqueDAO.save(nouvelleRecharge);
        }
    }

    public void activerRecharge(Long rechargeId) {
        RechargeAutomatique recharge = rechargeAutomatiqueDAO.findById(rechargeId);
        if (recharge != null) {
            recharge.setStatut("ACTIVE");
            rechargeAutomatiqueDAO.update(recharge);
        }
    }

    public void desactiverRecharge(Long rechargeId) {
        RechargeAutomatique recharge = rechargeAutomatiqueDAO.findById(rechargeId);
        if (recharge != null) {
            recharge.setStatut("INACTIVE");
            rechargeAutomatiqueDAO.update(recharge);
        }
    }

    public void supprimerRecharge(Long rechargeId) {
        RechargeAutomatique recharge = rechargeAutomatiqueDAO.findById(rechargeId);
        if (recharge != null) {
            rechargeAutomatiqueDAO.delete(recharge);
        }
    }
} 