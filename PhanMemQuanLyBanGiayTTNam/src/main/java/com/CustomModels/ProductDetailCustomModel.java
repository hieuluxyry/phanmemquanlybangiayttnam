package com.CustomModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailCustomModel {
    private String maSP;
    private String tenSP;
    private String loaiSP;
    private String kichCo;
    private String mauSac;
    private String chatLieu;
    private float donGia;
    private int soLuong;
    private String moTa;
}
