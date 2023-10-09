package com.Services;

import com.Models.Substance;

import java.util.List;

public interface SubstanceService {

    public List<Substance> getList();

    public boolean insert(Substance substance);

    public boolean update(Substance substance, String code);

    public boolean delete(String code);

}
