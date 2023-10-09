package com.Services.Implements;

import com.CustomModels.BillDetailCustomModel;
import com.CustomModels.BillDetailWithProductDetailCustomModel;
import com.CustomModels.ProductDetailCustomModel;
import com.Models.BillDetails;
import com.Repositories.BillDetailRepository;
import com.Services.BillDetailService;

import java.sql.Date;
import java.util.List;

public class BillDetailServiceImplement implements BillDetailService {

    private BillDetailRepository billDetailRepository = new BillDetailRepository();

    @Override
    public boolean insert(BillDetails billDetails) {
        return billDetailRepository.insert(billDetails);
    }

    @Override
    public List<BillDetailCustomModel> getListBill(String code) {
        return billDetailRepository.getListBill(code);
    }

    @Override
    public boolean checkProducts(String codeSP, String codeHD) {
        return billDetailRepository.checkProducts(codeSP, codeHD);
    }

    @Override
    public Integer getSoLuong(String codeSP, String codeHD) {
        return billDetailRepository.getSoLuong(codeSP, codeHD);
    }

    @Override
    public boolean update(String codeSp, String codeHd, int soLuong) {
        return billDetailRepository.update(codeSp, codeHd, soLuong);
    }

    @Override
    public Double sumDonGia(String codeHD) {
        return billDetailRepository.sumDonGia(codeHD);
    }

    @Override
    public List<BillDetailWithProductDetailCustomModel> getListThongKe(String ngayBatDau, String ngayKeThuc) {
        return billDetailRepository.getListThongKe(ngayBatDau, ngayKeThuc);
    }

    @Override
    public boolean delete(String idBill, String idProduct) {
        return billDetailRepository.delete(idBill, idProduct);
    }

    @Override
    public List<Double> sumDate(Date date) {
        return billDetailRepository.sumDate(date);
    }

    @Override
    public List<Double> sumMonth(int date) {
        return billDetailRepository.sum(date, billDetailRepository.sumMonth);
    }

    @Override
    public List<Double> sumYear(int date) {
        return billDetailRepository.sum(date, billDetailRepository.sumYear);
    }

    @Override
    public List<BillDetails> getList() {
        return billDetailRepository.getList();
    }

    @Override
    public List<ProductDetailCustomModel> getListBillPanelLichSu(String code) {
        return billDetailRepository.getListBillPanelLichSu(code);
    }

    @Override
    public List<Integer> getListDoanhThu(int year) {
        return billDetailRepository.getListDoanhThu(year);
    }

    @Override
    public Long getSoLuongDoanhThu(int month) {
        return billDetailRepository.getSoLuongDoanhThu(month);
    }

    @Override
    public Double getGiaBan(int month) {
        return billDetailRepository.getGiaBan(month);
    }

    @Override
    public Double getGiaGiam(int month) {
        return billDetailRepository.getGiaGiam(month);
    }
}
