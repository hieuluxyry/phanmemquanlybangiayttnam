package com.Services;

import com.Models.Promotion;

import java.sql.Date;
import java.util.List;

public interface PromotionService {

    public List<Promotion> getListOn();

    public List<Promotion> getListOff();

    public List<Promotion> getList();

    public boolean insert(Promotion promotion);

    public boolean update(String code, Promotion promotion);

    public boolean hideOrShow(String code, int status);

    public boolean hideDate(Date date, int status);

    public String getByID(String code);
}
