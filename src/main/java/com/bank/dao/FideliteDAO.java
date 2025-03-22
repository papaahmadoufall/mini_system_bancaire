package com.bank.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bank.model.Fidelite;
import com.bank.util.HibernateUtil;

public class FideliteDAO {
    public void save(Fidelite fidelite) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(fidelite);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public Fidelite findByClientId(Long clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Fidelite f where f.client.id = :clientId", Fidelite.class)
                    .setParameter("clientId", clientId)
                    .uniqueResult();
        }
    }
} 