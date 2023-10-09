package com.Repositories;

import com.CustomModels.StaffCustomModel;
import com.Models.Staff;
import com.Utilities.HibernateUtil;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class StaffRepository {

    // Panel Nhân Viên
    public List<StaffCustomModel> getList(int status) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery(
                "select new com.CustomModels.StaffCustomModel("
                + " s.code, concat(s.lastName, ' ', s.firstName), s.dateOfBirth, s.sex, s.phoneNumber, s.email, s.address, s.role) "
                + " from com.Models.Staff s where s.status = " + status);
        List<StaffCustomModel> list = query.getResultList();
        return list;
    }

    public boolean insert(Staff staff) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.save(staff);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(String code, Staff staff) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update Staff set firstName =: firstName, lastName =: lastName"
                    + ", dateOfBirth =: ngaySinh, phoneNumber =: sdt, email =: email, address =: diaChi,sex =:gt,"
                    + " role =: chucVu where code =: code");
            query.setParameter("firstName", staff.getFirstName());
            query.setParameter("lastName", staff.getLastName());
            query.setParameter("ngaySinh", staff.getDateOfBirth());
            query.setParameter("gt", staff.getSex());
            query.setParameter("sdt", staff.getPhoneNumber());
            query.setParameter("email", staff.getEmail());
            query.setParameter("diaChi", staff.getAddress());
            query.setParameter("chucVu", staff.getRole());
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
            Query query = session.createQuery("update Staff set  status =: status where code =: code");
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

    // Panel Cửa Hàng
    public String getByFirstName = ("SELECT s.firstName FROM Staff s WHERE s.code =: code");
    public String getByLastName = ("SELECT s.lastName FROM Staff s WHERE s.code =: code");

    public String getByName(String code, String statement) {
        String uuid;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<String> query = session.createQuery(statement, String.class);
            query.setParameter("code", code);
            uuid = query.getSingleResult();
        }
        return uuid;
    }

    // Panel Login
    public List<Staff> getAccountStaff(String email, String password) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from Staff where email =: email and password =: password");
        query.setParameter("email", email);
        query.setParameter("password", password);
        List<Staff> list = query.getResultList();
        return list;
    }

    public boolean checkAccountStaff(String email) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from Staff where email =: email");
        query.setParameter("email", email);
        List<Staff> list = query.getResultList();
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }

    public String getByPassword(String email) {
        String password;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            TypedQuery<String> query = session.createQuery("select password from Staff where email =: email", String.class);
            query.setParameter("email", email);
            password = query.getSingleResult();
        }
        return password;
    }

    // Panel QR Code
    public boolean checkAccountStaffQR(String code) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from Staff where code =: code");
        query.setParameter("code", code);
        List<Staff> list = query.getResultList();
        if (list.isEmpty()) {
            return false;
        }
        return true;
    }

    public List<Staff> getAccountStaffQR(String code) {
        Session session = HibernateUtil.getFACTORY().openSession();
        Query query = session.createQuery("from Staff where code =: code");
        query.setParameter("code", code);
        List<Staff> list = query.getResultList();
        return list;
    }
    public boolean updatePassword(String username, String password) {
        Transaction transaction = null;
        try ( Session session = HibernateUtil.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("update Staff set password =: password where email =: email");
            query.setParameter("password", password);
            query.setParameter("email", username);
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
            TypedQuery<String> query = session.createQuery("select id from Staff where code =: code", String.class);
            query.setParameter("code", code);
            id = query.getSingleResult();
        }
        return id;
    }
}
