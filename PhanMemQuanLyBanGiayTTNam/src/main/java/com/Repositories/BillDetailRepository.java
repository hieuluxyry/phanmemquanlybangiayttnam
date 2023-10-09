package com.Repositories;

import com.CustomModels.BillDetailCustomModel;
import com.CustomModels.BillDetailWithProductDetailCustomModel;
import com.CustomModels.ProductDetailCustomModel;
import com.Models.BillDetails;
import com.Utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.List;

public class BillDetailRepository {

    public boolean insert(BillDetails billDetails) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.save(billDetails);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String idBill, String idProduct) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("delete from BillDetails where bill.id =: idBill and productDetails.id =: idProduct");
            query.setParameter("idBill", idBill);
            query.setParameter("idProduct", idProduct);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(String codeSP, String codeHD, int soLuong) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update BillDetails set amount =:soLuong where productDetails.id =: codeSP and bill.id =: codeHD");
            query.setParameter("codeSP", codeSP);
            query.setParameter("codeHD", codeHD);
            query.setParameter("soLuong", soLuong);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<BillDetailCustomModel> getListBill(String code) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("select new com.CustomModels.BillDetailCustomModel(b.productDetails.code, b.productDetails.name, b.amount, b.productDetails.exportPrice, b.promotion.decreaseNumber) from com.Models.BillDetails b where b.bill.code =: code");
        query.setParameter("code", code);
        List<BillDetailCustomModel> list = query.getResultList();
        return list;
    }

    public List<BillDetails> getList() {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from BillDetails");
        List<BillDetails> list = query.getResultList();
        return list;
    }

    public boolean checkProducts(String codeSP, String codeHD) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from BillDetails where productDetails.code =: codeSP and bill.code =: codeHD");
        query.setParameter("codeSP", codeSP);
        query.setParameter("codeHD", codeHD);
        List<BillDetails> list = query.getResultList();
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }

    public Integer getSoLuong(String codeSP, String codeHD) {
        Integer id;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<Integer> query = session.createQuery("select amount from BillDetails where productDetails.code =: codeSP and bill.code =: codeHD", Integer.class);
            query.setParameter("codeSP", codeSP);
            query.setParameter("codeHD", codeHD);
            id = query.getSingleResult();
        }
        return id;
    }

    public Double sumDonGia(String codeHD) {
        Double id;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<Double> query = session.createQuery("select sum(amount * (unitPrice - (unitPrice / 100 * promotion.decreaseNumber))) from BillDetails where bill.code =: codeHD", Double.class);
            query.setParameter("codeHD", codeHD);
            id = query.getSingleResult();
        }
        return id;
    }

    public List<BillDetailWithProductDetailCustomModel> getListThongKe(String ngayBatDau, String ngayKetThuc) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("select new com.CustomModels.BillDetailWithProductDetailCustomModel(b.productDetails.code, sum(b.amount) as tong) from com.Models.BillDetails b where b.bill.dateOfPayment between '" + ngayBatDau + "' and '" + ngayKetThuc + "' group by b.productDetails.code order by tong desc");
        List<BillDetailWithProductDetailCustomModel> list = query.getResultList();
        return list;
    }

    public List<Double> sumDate(Date date) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("select sum(amount * (unitPrice - (unitPrice / 100 * promotion.decreaseNumber))) from BillDetails b where b.bill.dateOfPayment =: date");
        query.setParameter("date", date);
        List<Double> list = query.getResultList();
        return list;
    }

    public String sumMonth = "select sum(amount * (unitPrice - (unitPrice / 100 * promotion.decreaseNumber))) from BillDetails b where month(b.bill.dateOfPayment) = ";

    public String sumYear = "select sum(amount * (unitPrice - (unitPrice / 100 * promotion.decreaseNumber))) from BillDetails b where year(b.bill.dateOfPayment) = ";

    public List<Double> sum(int date, String select) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery(select + date);
        List<Double> list = query.getResultList();
        return list;
    }

    // Panel Lịch Sử

    public List<ProductDetailCustomModel> getListBillPanelLichSu(String code) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("select new com.CustomModels.ProductDetailCustomModel(b.productDetails.code, b.productDetails.name, b.productDetails.productType.name, b.productDetails.size.name, b.productDetails.color.name, b.productDetails.substance.name, b.productDetails.exportPrice, b.amount, b.productDetails.describe) from com.Models.BillDetails b where b.bill.code =: code");
        query.setParameter("code", code);
        List<ProductDetailCustomModel> list = query.getResultList();
        return list;
    }

    public List<Integer> getListDoanhThu(int year) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("select distinct month(b.bill.dateOfPayment) from BillDetails b where year(b.bill.dateOfPayment) =: year");
        query.setParameter("year", year);
        List<Integer> list = query.getResultList();
        return list;
    }

    public Long getSoLuongDoanhThu(int month) {
        Long id;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<Long> query = session.createQuery("select sum(b.amount) from BillDetails b where month(b.bill.dateOfPayment) = " + month, Long.class);
            id = query.getSingleResult();
        }
        return id;
    }

    public Double getGiaBan(int month) {
        Double id;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<Double> query = session.createQuery("select sum(b.amount * b.unitPrice) from BillDetails b where month(b.bill.dateOfPayment) = " + month, Double.class);
            id = query.getSingleResult();
        }
        return id;
    }

    public Double getGiaGiam(int month) {
        Double id;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<Double> query = session.createQuery("select sum(b.amount * (b.unitPrice / 100 * b.promotion.decreaseNumber)) from BillDetails b where month(b.bill.dateOfPayment) = " + month, Double.class);
            id = query.getSingleResult();
        }
        return id;
    }
}
