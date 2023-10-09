package com.Services.Implements;

import com.Models.PromotionDetails;
import com.Repositories.PromotionDetailRepository;
import com.Services.PromotionDetailService;

public class PromotionDetailServiceImplement implements PromotionDetailService {

    private PromotionDetailRepository promotionDetailRepository = new PromotionDetailRepository();

    @Override
    public boolean checkBoxSanPham(String codeSP, String codeKM) {
        return promotionDetailRepository.checkBoxSanPham(codeSP, codeKM);
    }

    @Override
    public boolean insert(PromotionDetails promotionDetails) {
        return promotionDetailRepository.insert(promotionDetails);
    }

    @Override
    public boolean delete(String codeSP, String codeKM) {
        return promotionDetailRepository.delete(codeSP, codeKM);
    }

    @Override
    public String getByID(String code) {
        if (!promotionDetailRepository.check(code, 1)){
            return promotionDetailRepository.getByID(code, 3);
        }
        return promotionDetailRepository.getByID(code, 1);
    }
}
