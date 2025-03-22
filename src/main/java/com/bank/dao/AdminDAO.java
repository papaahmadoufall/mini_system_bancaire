package com.bank.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bank.model.Admin;
import com.bank.util.HibernateUtil;

public class AdminDAO {
    public void save(Admin admin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(admin);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public void update(Admin admin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(admin);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public Admin findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Admin.class, id);
        }
    }

    public Admin findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Admin a where a.username = :username", Admin.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
    }

    public List<Admin> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Admin", Admin.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(Admin admin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(admin);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }
} 