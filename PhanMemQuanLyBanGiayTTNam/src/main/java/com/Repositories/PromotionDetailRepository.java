package com.Repositories;

import com.Models.BillDetails;
import com.Models.PromotionDetails;
import com.Utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class PromotionDetailRepository {

    public boolean checkBoxSanPham(String codeSP, String codeKM) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from PromotionDetails where productDetails.code =: codeSP and promotion.code =: codeKM");
        query.setParameter("codeSP", codeSP);
        query.setParameter("codeKM", codeKM);
        List<PromotionDetails> list = query.getResultList();
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean insert(PromotionDetails promotionDetails) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.save(promotionDetails);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String codeSP, String codeKM) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("delete from PromotionDetails where productDetails.id =: codeSP and promotion.id =: codeKM");
            query.setParameter("codeSP", codeSP);
            query.setParameter("codeKM", codeKM);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getByID(String code, int status) {
        String id;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<String> query = session.createQuery("select promotion.id from PromotionDetails where productDetails.id =: code and promotion.status =: status", String.class);
            query.setParameter("code", code);
            query.setParameter("status", status);
            id = query.getSingleResult();
        }
        return id;
    }

    public boolean check(String code, int status) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("select promotion.id from PromotionDetails where productDetails.id =: code and promotion.status =: status");
        query.setParameter("code", code);
        query.setParameter("status", status);
        List<String> list = query.getResultList();
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }
}
