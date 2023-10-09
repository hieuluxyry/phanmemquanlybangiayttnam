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
public class StaffCustomModel {

    private String maNV;
    private String hoTen;
    private Date ngaySinh;
    private String gioiTinh;
    private String sdt;
    private String email;
    private String diaChi;
    private int chucVu;

}
