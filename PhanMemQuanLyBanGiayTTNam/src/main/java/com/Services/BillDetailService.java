package com.Services;

import com.CustomModels.BillDetailCustomModel;
import com.CustomModels.BillDetailWithProductDetailCustomModel;
import com.CustomModels.ProductDetailCustomModel;
import com.Models.BillDetails;

import java.sql.Date;
import java.util.List;

public interface BillDetailService {

    public boolean insert(BillDetails billDetails);

    public List<BillDetailCustomModel> getListBill(String code);

    public boolean checkProducts(String codeSP, String codeHD);

    public Integer getSoLuong(String codeSP, String codeHD);

    public boolean update(String codeSp, String codeHd, int soLuong);

    public Double sumDonGia(String codeHD);

    public List<BillDetailWithProductDetailCustomModel> getListThongKe(String ngayBatDau, String ngayKetThuc);

    public boolean delete(String idBill, String idProduct);

    public List<Double> sumDate(Date date);

    public List<Double> sumMonth(int date);

    public List<Double> sumYear(int date);

    public List<BillDetails> getList();

    public List<ProductDetailCustomModel> getListBillPanelLichSu(String code);

    public List<Integer> getListDoanhThu(int year);

    public Long getSoLuongDoanhThu(int month);

    public Double getGiaBan(int month);

    public Double getGiaGiam(int month);
}
