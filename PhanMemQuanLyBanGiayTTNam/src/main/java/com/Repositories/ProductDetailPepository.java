package com.Repositories;

import com.CustomModels.ProductDetailCustomModel;
import com.Models.ProductDetails;
import com.Utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProductDetailPepository {

    public List<ProductDetailCustomModel> getListProductDetail(int status) {
        String select = "select new com.CustomModels.ProductDetailCustomModel(pd.code, pd.name, pd.productType.name, pd.size.name, pd.color.name, pd.substance.name, pd.exportPrice, pd.amount, pd.describe) from com.Models.ProductDetails pd where status = " + status;
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery(select);
        List<ProductDetailCustomModel> list = query.getResultList();
        return list;
    }

    public boolean insert(ProductDetails productDetails) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.save(productDetails);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSoLuong(String code, int soLuong) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update ProductDetails set amount =: soLuong where code =: code");
            query.setParameter("soLuong", soLuong);
            query.setParameter("code", code);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(ProductDetails productDetails, String code) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update ProductDetails set name =: Name, productType =: ProductType, size =: Size, color =: Color, substance =: Substance, exportPrice =: ExportPrice, amount =: Amount, describe =: Describe where code =: Code");
            query.setParameter("Name", productDetails.getName());
            query.setParameter("ProductType", productDetails.getProductType());
            query.setParameter("Size", productDetails.getSize());
            query.setParameter("Color", productDetails.getColor());
            query.setParameter("Substance", productDetails.getSubstance());
            query.setParameter("ExportPrice", productDetails.getExportPrice());
            query.setParameter("Amount", productDetails.getAmount());
            query.setParameter("Describe", productDetails.getDescribe());
            query.setParameter("Code", code);
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
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update ProductDetails set status =: Status where code =: Code");
            query.setParameter("Status", status);
            query.setParameter("Code", code);
            query.executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<ProductDetails> getList() {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from ProductDetails");
        List<ProductDetails> list = query.getResultList();
        return list;
    }

    public String getListByCode = "select new com.CustomModels.ProductDetailCustomModel(pd.code, pd.name, pd.productType.name, pd.size.name, pd.color.name, pd.substance.name, pd.exportPrice, pd.amount, pd.describe) from com.Models.ProductDetails pd where code =: code";
    public String getListByName = "select new com.CustomModels.ProductDetailCustomModel(pd.code, pd.name, pd.productType.name, pd.size.name, pd.color.name, pd.substance.name, pd.exportPrice, pd.amount, pd.describe) from com.Models.ProductDetails pd where name =: name";
    public String getListByProductType = "select new com.CustomModels.ProductDetailCustomModel(pd.code, pd.name, pd.productType.name, pd.size.name, pd.color.name, pd.substance.name, pd.exportPrice, pd.amount, pd.describe) from com.Models.ProductDetails pd where productType.name =: productType";
    public String getListBySize = "select new com.CustomModels.ProductDetailCustomModel(pd.code, pd.name, pd.productType.name, pd.size.name, pd.color.name, pd.substance.name, pd.exportPrice, pd.amount, pd.describe) from com.Models.ProductDetails pd where pd.size.name =: size";
    public String getListByColor = "select new com.CustomModels.ProductDetailCustomModel(pd.code, pd.name, pd.productType.name, pd.size.name, pd.color.name, pd.substance.name, pd.exportPrice, pd.amount, pd.describe) from com.Models.ProductDetails pd where pd.color.name =: color";
    public String getListBySubstance = "select new com.CustomModels.ProductDetailCustomModel(pd.code, pd.name, pd.productType.name, pd.size.name, pd.color.name, pd.substance.name, pd.exportPrice, pd.amount, pd.describe) from com.Models.ProductDetails pd where pd.substance.name =: substance";

    public List<ProductDetailCustomModel> getListByCode(String code) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery(getListByCode);
        query.setParameter("code", code);
        List<ProductDetailCustomModel> list = query.getResultList();
        return list;
    }

    public List<ProductDetailCustomModel> getListByName(String name) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery(getListByName);
        query.setParameter("name", name);
        List<ProductDetailCustomModel> list = query.getResultList();
        return list;
    }

    public List<ProductDetailCustomModel> getListByProductType(String type) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery(getListByProductType);
        query.setParameter("productType", type);
        List<ProductDetailCustomModel> list = query.getResultList();
        return list;
    }

    public List<ProductDetailCustomModel> getListBySize(String size) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery(getListBySize);
        query.setParameter("size", size);
        List<ProductDetailCustomModel> list = query.getResultList();
        return list;
    }

    public List<ProductDetailCustomModel> getListByColor(String color) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery(getListByColor);
        query.setParameter("color", color);
        List<ProductDetailCustomModel> list = query.getResultList();
        return list;
    }

    public List<ProductDetailCustomModel> getListBySubstance(String substance) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery(getListBySubstance);
        query.setParameter("substance", substance);
        List<ProductDetailCustomModel> list = query.getResultList();
        return list;
    }

    public String getByID(String code) {
        String id;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<String> query = session.createQuery("select id from ProductDetails where code =: code", String.class);
            query.setParameter("code", code);
            id = query.getSingleResult();
        }
        return id;
    }

    public Float getByDonGia(String code) {
        Float id;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<Float> query = session.createQuery("select exportPrice from ProductDetails where code =: code", Float.class);
            query.setParameter("code", code);
            id = query.getSingleResult();
        }
        return id;
    }

    public Integer getBySoLuong(String code) {
        Integer id;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<Integer> query = session.createQuery("select amount from ProductDetails where code =: code", Integer.class);
            query.setParameter("code", code);
            id = query.getSingleResult();
        }
        return id;
    }

    public String getByNameProduct = "select name from ProductDetails where code =: code";
    public String getByNameType = "select productType.name from ProductDetails where code =: code";
    public String getByNameSize = "select size.name from ProductDetails where code =: code";
    public String getByNameColor = "select color.name from ProductDetails where code =: code";
    public String getByNameSubtance = "select substance.name from ProductDetails where code =: code";

    public String getByName(String code, String select) {
        String id;
        try (Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<String> query = session.createQuery(select, String.class);
            query.setParameter("code", code);
            id = query.getSingleResult();
        }
        return id;
    }
}
