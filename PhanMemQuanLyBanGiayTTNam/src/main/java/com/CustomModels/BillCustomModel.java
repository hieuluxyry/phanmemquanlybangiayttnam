package com.CustomModels;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillCustomModel {

    private String maHD;
    private String tenKH;
    private String tenNV;
    private Date ngayTao;
    private int trangThai;

    public String trangThaiHD(int trangThai) {
        switch (trangThai) {
            case 1 -> {
                return "Đã Thanh Toán";
            }
            case 2 -> {
                return "Đã hủy";
            }
            case 3 -> {
                return "Đang giao hàng";
            }
            case 0 -> {
                return "Chờ thanh toán";
            }
        }
        return null;
    }
}
