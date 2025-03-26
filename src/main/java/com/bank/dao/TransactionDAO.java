package com.bank.dao;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bank.model.BankTransaction;
import com.bank.model.CarteBancaire;
import com.bank.model.Client;
import com.bank.util.DatabaseUtil;

public class TransactionDAO {
    public void save(BankTransaction transaction) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction hibernateTx = session.beginTransaction();
            session.persist(transaction);
            hibernateTx.commit();
        }
    }

    public void update(BankTransaction transaction) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction hibernateTx = session.beginTransaction();
            session.merge(transaction);
            hibernateTx.commit();
        }
    }

    public void delete(BankTransaction transaction) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction hibernateTx = session.beginTransaction();
            session.remove(transaction);
            hibernateTx.commit();
        }
    }

    public BankTransaction findById(Long id) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.get(BankTransaction.class, id);
        }
    }

    public List<BankTransaction> findAll() {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery("from BankTransaction", BankTransaction.class)
                    .getResultList();
        }
    }

    public List<BankTransaction> findByCarteId(Long carteId) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery("from BankTransaction t where t.carte.id = :carteId", BankTransaction.class)
                    .setParameter("carteId", carteId)
                    .getResultList();
        }
    }

    public List<BankTransaction> findByDate(LocalDate date) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery("from BankTransaction t where t.date = :date", BankTransaction.class)
                    .setParameter("date", date)
                    .getResultList();
        }
    }

    public List<BankTransaction> findTransactionsSuspectes(double seuil) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery("from BankTransaction t where t.montant > :seuil and t.statut = 'SUSPECTE'", BankTransaction.class)
                    .setParameter("seuil", seuil)
                    .getResultList();
        }
    }

    public List<BankTransaction> findByClient(Client client) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery(
                    "select t from BankTransaction t join t.carte c where c.client = :client",
                    BankTransaction.class)
                    .setParameter("client", client)
                    .getResultList();
        }
    }

    public List<BankTransaction> findByCarte(CarteBancaire carte) {
        try (Session session = DatabaseUtil.getSession()) {
            return session.createQuery(
                    "from BankTransaction where carte = :carte",
                    BankTransaction.class)
                    .setParameter("carte", carte)
                    .getResultList();
        }
    }

    public long count() {
        try (Session session = DatabaseUtil.getSession()) {
            return (Long) session.createQuery("SELECT COUNT(*) FROM BankTransaction").uniqueResult();
        }
    }
} 