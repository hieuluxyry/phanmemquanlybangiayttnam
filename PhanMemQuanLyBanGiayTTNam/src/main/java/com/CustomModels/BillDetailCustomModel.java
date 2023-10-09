package com.CustomModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailCustomModel {
    private String maSP;
    private String tenSP;
    private int soLuong;
    private float donGia;
    private int phanTramKM;
}
