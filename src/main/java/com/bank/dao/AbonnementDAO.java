package com.bank.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bank.model.Abonnement;
import com.bank.model.CarteBancaire;
import com.bank.model.Client;
import com.bank.util.DatabaseUtil;

public class AbonnementDAO {
    public void save(Abonnement abonnement) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(abonnement);
            transaction.commit();
        }
    }

    public Abonnement findById(Long id) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.get(Abonnement.class, id);
        }
    }

    public List<Abonnement> findByCarte(CarteBancaire carte) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery(
                "from Abonnement where carte = :carte", 
                Abonnement.class)
                .setParameter("carte", carte)
                .getResultList();
        }
    }

    public List<Abonnement> findByClient(Client client) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery(
                "select a from Abonnement a join a.carte c where c.client = :client",
                Abonnement.class)
                .setParameter("client", client)
                .getResultList();
        }
    }

    public void update(Abonnement abonnement) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(abonnement);
            transaction.commit();
        }
    }

    public void delete(Abonnement abonnement) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(abonnement);
            transaction.commit();
        }
    }
} 