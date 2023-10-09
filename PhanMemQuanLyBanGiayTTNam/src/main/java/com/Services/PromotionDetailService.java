package com.Services;

import com.Models.PromotionDetails;

public interface PromotionDetailService {

    public boolean checkBoxSanPham(String codeSP, String codeKM);

    public boolean insert(PromotionDetails promotionDetails);

    public boolean delete(String codeSP, String codeKM);

    public String getByID(String code);
}
