package com.Services.Implements;

import com.Models.Promotion;
import com.Repositories.PromotionRepository;
import com.Services.PromotionService;

import java.sql.Date;
import java.util.List;

public class PromotionServiceImplement implements PromotionService {

    private PromotionRepository promotionRepository = new PromotionRepository();

    @Override
    public List<Promotion> getListOn() {
        return promotionRepository.getListPromotion(1);
    }

    @Override
    public List<Promotion> getListOff() {
        return promotionRepository.getListPromotion(0);
    }

    @Override
    public List<Promotion> getList() {
        return promotionRepository.getList();
    }

    @Override
    public boolean insert(Promotion promotion) {
        return promotionRepository.insert(promotion);
    }

    @Override
    public boolean update(String code, Promotion promotion) {
        return promotionRepository.update(code, promotion);
    }

    @Override
    public boolean hideOrShow(String code, int status) {
        return promotionRepository.hideOrShow(code, status);
    }

    @Override
    public boolean hideDate(Date date, int status) {
        return promotionRepository.hideDate(date, status);
    }

    @Override
    public String getByID(String code) {
        return promotionRepository.getByID(code);
    }
}
