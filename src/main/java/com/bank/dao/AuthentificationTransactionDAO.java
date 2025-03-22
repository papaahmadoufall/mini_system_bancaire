package com.bank.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bank.model.AuthentificationTransaction;
import com.bank.model.BankTransaction;
import com.bank.util.DatabaseUtil;

public class AuthentificationTransactionDAO {
    public void save(AuthentificationTransaction auth) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(auth);
            transaction.commit();
        }
    }

    public AuthentificationTransaction findById(Long id) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.get(AuthentificationTransaction.class, id);
        }
    }

    public List<AuthentificationTransaction> findByTransaction(BankTransaction transaction) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery(
                "from AuthentificationTransaction where transaction = :transaction", 
                AuthentificationTransaction.class)
                .setParameter("transaction", transaction)
                .getResultList();
        }
    }

    public void update(AuthentificationTransaction auth) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(auth);
            transaction.commit();
        }
    }

    public void delete(AuthentificationTransaction auth) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(auth);
            transaction.commit();
        }
    }
} 