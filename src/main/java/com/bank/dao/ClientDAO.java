package com.bank.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bank.model.Client;
import com.bank.util.DatabaseUtil;

public class ClientDAO {
    public void save(Client client) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(client);
            transaction.commit();
        }
    }

    public Client findById(Long id) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.get(Client.class, id);
        }
    }

    public List<Client> findAll() {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery("from Client", Client.class).list();
        }
    }

    public void update(Client client) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(client);
            transaction.commit();
        }
    }

    public void delete(Client client) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(client);
            transaction.commit();
        }
    }

    public Client findByEmail(String email) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery("from Client where email = :email", Client.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }
} 