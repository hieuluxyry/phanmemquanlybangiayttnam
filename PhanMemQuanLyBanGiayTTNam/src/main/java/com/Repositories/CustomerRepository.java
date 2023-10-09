package com.Repositories;

import com.Models.Customer;
import com.Utilities.HibernateUtil;

import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class CustomerRepository {

    public List<Customer> getList() {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from Customer");
        List<Customer> list = query.getResultList();
        return list;
    }

    public boolean insert(Customer customer) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.save(customer);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Customer customer, String code) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update Customer set email =:Email, firstName =:FirstName, lastName =:LastName, phoneNumber =: PhoneNumber where code =: Code");
            query.setParameter("Email", customer.getEmail());
            query.setParameter("FirstName", customer.getFirstName());
            query.setParameter("LastName", customer.getLastName());
            query.setParameter("PhoneNumber", customer.getPhoneNumber());
            query.setParameter("Code", code);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String code) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("delete from Customer where code =: Code");
            query.setParameter("Code", code);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Customer> search(String phoneNumber) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from Customer where phoneNumber like : phoneNumber");
        query.setParameter("phoneNumber", "%" + phoneNumber + "%");
        List<Customer> list = query.getResultList();
        return list;
    }

    public String getByFirstName = ("SELECT firstName FROM Customer WHERE code =: code");
    public String getByLastName = ("SELECT lastName FROM Customer WHERE code =: code");
    public String getByName = ("SELECT lastName FROM Customer WHERE phoneNumber =: code");

    public String getByName(String code, String statement) {
        String uuid;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<String> query = session.createQuery(statement, String.class);
            query.setParameter("code", code);
            uuid = query.getSingleResult();
        }
        return uuid;
    }

    public String getByID = "select id from Customer where phoneNumber =: phone";
    public String getByEmail = "select email from Customer where phoneNumber =: phone";

    public String getBy(String phone, String select) {
        String id;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<String> query = session.createQuery(select, String.class);
            query.setParameter("phone", phone);
            id = query.getSingleResult();
        }
        return id;
    }

    public boolean checkCustomer(String phone) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from Customer where phoneNumber =: phone");
        query.setParameter("phone", phone);
        List<Customer> list = query.getResultList();
        if (list.isEmpty()){
            return false;
        }
        return true;
    }
}
