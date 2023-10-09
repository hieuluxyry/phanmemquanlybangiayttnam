package com.Repositories;

import com.Models.ProductType;
import com.Utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.List;

public class ProductTypeRepository {

    public List<ProductType> getList() {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from ProductType");
        List<ProductType> list = query.getResultList();
        return list;
    }

    public boolean insert(ProductType productType) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.save(productType);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(ProductType productType, String code) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update ProductType set name =: Name where code =: Code");
            query.setParameter("Name", productType.getName());
            query.setParameter("Code", code);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("delete from ProductType where id =: Id");
            query.setParameter("Id", id);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
