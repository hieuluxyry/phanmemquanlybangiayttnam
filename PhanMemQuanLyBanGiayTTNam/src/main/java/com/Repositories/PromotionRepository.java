package com.Repositories;

import com.Models.Promotion;
import com.Utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.List;

public class PromotionRepository {

    public List<Promotion> getListPromotion(int status) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from Promotion where status = " + status);
        List<Promotion> list = query.getResultList();
        return list;
    }

    public List<Promotion> getList() {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from Promotion");
        List<Promotion> list = query.getResultList();
        return list;
    }

    public boolean insert(Promotion promotion) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.save(promotion);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(String code, Promotion promotion) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update Promotion set code =: code, name =: name, "
                    + "decreaseNumber =: decreaseNumber, startDay =: startDay, endDay =: endDay, "
                    + "status =: status where code =: code");
            query.setParameter("code", promotion.getCode());
            query.setParameter("name", promotion.getName());
            query.setParameter("decreaseNumber", promotion.getDecreaseNumber());
            query.setParameter("startDay", promotion.getStartDay());
            query.setParameter("endDay", promotion.getEndDay());
            query.setParameter("status", promotion.getStatus());
            query.setParameter("code", code);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hideOrShow(String code, int status) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update Promotion set status =: status where code =: code");
            query.setParameter("status", status);
            query.setParameter("code", code);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hideDate(Date date, int status) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update Promotion set status =: status where endDay <: date");
            query.setParameter("status", status);
            query.setParameter("date", date);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getByID(String code) {
        String id;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<String> query = session.createQuery("select id from Promotion where code =: code", String.class);
            query.setParameter("code", code);
            id = query.getSingleResult();
        }
        return id;
    }
}
