package com.bank.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bank.model.RechargeAutomatique;
import com.bank.util.DatabaseUtil;

public class RechargeAutomatiqueDAO {
    public void save(RechargeAutomatique recharge) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(recharge);
            transaction.commit();
        }
    }

    public void update(RechargeAutomatique recharge) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(recharge);
            transaction.commit();
        }
    }

    public void delete(RechargeAutomatique recharge) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(recharge);
            transaction.commit();
        }
    }

    public RechargeAutomatique findById(Long id) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.get(RechargeAutomatique.class, id);
        }
    }

    public List<RechargeAutomatique> findAll() {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery("from RechargeAutomatique", RechargeAutomatique.class)
                    .getResultList();
        }
    }

    public List<RechargeAutomatique> findByCarteId(Long carteId) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery("from RechargeAutomatique r where r.carte.id = :carteId", RechargeAutomatique.class)
                    .setParameter("carteId", carteId)
                    .getResultList();
        }
    }

    public List<RechargeAutomatique> findActive() {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery("from RechargeAutomatique r where r.statut = 'ACTIVE'", RechargeAutomatique.class)
                    .getResultList();
        }
    }
} 