package com.Services.Implements;

import com.CustomModels.BillCustomModel;
import com.Models.Bill;
import com.Repositories.BillRepository;
import com.Services.BillService;

import java.sql.Date;
import java.util.List;

public class BillServiceImplement implements BillService {

    private BillRepository billRepository = new BillRepository();

    @Override
    public List<Bill> getList() {
        return billRepository.getList();
    }

    @Override
    public List<Bill> getList(String code) {
        return billRepository.getList(code);
    }

    @Override
    public List<BillCustomModel> getListBill() {
        return billRepository.getListBill();
    }

    @Override
    public List<BillCustomModel> getListBill(String code) {
        return billRepository.getListBill(code);
    }

    @Override
    public boolean insert(Bill bill) {
        return billRepository.insert(bill);
    }

    @Override
    public boolean updateThanhToan(String code, int status, Date ngay) {
        return billRepository.update(code, status, ngay, billRepository.ngayThanhToan);
    }

    @Override
    public boolean updateGiaoHang(String code, int status, Date ngayThanhToan) {
        return billRepository.update(code, status, ngayThanhToan, billRepository.ngayVanChuyen);
    }

    @Override
    public boolean updateDaNhan(String code, int status, Date ngayThanhToan) {
        return billRepository.update(code, status, ngayThanhToan, billRepository.ngayNhan);
    }

    @Override
    public boolean updateHuy(String code, String note, int status, Date date) {
        return billRepository.updateHuy(code, note, status, date);
    }


    @Override
    public String getByID(String code) {
        return billRepository.getByID(code);
    }


    @Override
    public List<Integer> getYear() {
        return billRepository.getYear();
    }

    @Override
    public boolean checkStatus(String code) {
        int check = billRepository.checkStatus(code);
        if (check == 1){
            return false;
        }
        return true;
    }

    @Override
    public List<Bill> sumAllThanhCong() {
        return billRepository.sumAll(1);
    }

    @Override
    public List<Bill> sumAllHuy() {
        return billRepository.sumAll(2);
    }

    @Override
    public List<Bill> sumNgayThanhCong(Date date) {
        return billRepository.sumNgay(1, date);
    }

    @Override
    public List<Bill> sumNgayHuy(Date date) {
        return billRepository.sumNgay(2, date);
    }

    @Override
    public List<Bill> sumThangThanhCong(int date) {
        return billRepository.sum(1, date, billRepository.sumThang);
    }

    @Override
    public List<Bill> sumThangHuy(int date) {
        return billRepository.sum(2, date, billRepository.sumThang);
    }

    @Override
    public List<Bill> sumNamThanhCong(int date) {
        return billRepository.sum(1, date, billRepository.sumNam);
    }

    @Override
    public List<Bill> sumNamHuy(int date) {
        return billRepository.sum(2, date, billRepository.sumNam);
    }
}
