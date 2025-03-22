package com.bank.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bank.model.Client;
import com.bank.model.Litige;
import com.bank.util.HibernateUtil;

public class LitigeDAO {
    public void save(Litige litige) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(litige);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Litige litige) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(litige);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Litige> findByClientId(Long clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Litige l WHERE l.transaction.carte.client.id = :clientId", Litige.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        }
    }

    public List<Litige> findByClient(Client client) {
        return findByClientId(client.getId());
    }

    public List<Litige> findByStatut(String statut) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Litige l WHERE l.statut = :statut", Litige.class)
                    .setParameter("statut", statut)
                    .getResultList();
        }
    }
} 