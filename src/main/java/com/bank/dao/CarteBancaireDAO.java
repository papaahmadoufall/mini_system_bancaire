package com.bank.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bank.model.CarteBancaire;
import com.bank.model.Client;
import com.bank.util.DatabaseUtil;

public class CarteBancaireDAO {
    public void save(CarteBancaire carte) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(carte);
            transaction.commit();
        }
    }

    public CarteBancaire findById(Long id) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.get(CarteBancaire.class, id);
        }
    }

    public List<CarteBancaire> findByClient(Client client) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery(
                "from CarteBancaire where client = :client", 
                CarteBancaire.class)
                .setParameter("client", client)
                .list();
        }
    }

    public void update(CarteBancaire carte) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(carte);
            transaction.commit();
        }
    }

    public void delete(CarteBancaire carte) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(carte);
            transaction.commit();
        }
    }

    public List<CarteBancaire> findAll() {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery("from CarteBancaire", CarteBancaire.class).list();
        }
    }

    public long count() {
        try (Session session = DatabaseUtil.getSession()) {
            return (Long) session.createQuery("SELECT COUNT(*) FROM CarteBancaire").uniqueResult();
        }
    }
} 