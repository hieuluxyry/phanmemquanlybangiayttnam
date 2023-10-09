package com.Services.Implements;

import com.Models.Substance;
import com.Repositories.SubstanceRepository;

import java.util.List;

import com.Services.SubstanceService;

public class SubstanceServiceImplement implements SubstanceService {

    private SubstanceRepository repository = new SubstanceRepository();

    @Override
    public List<Substance> getList() {
        return repository.getList();
    }

    @Override
    public boolean insert(Substance substance) {
        return repository.insert(substance);
    }

    @Override
    public boolean update(Substance substance, String code) {
        return repository.update(substance, code);
    }

    @Override
    public boolean delete(String code) {
        return repository.delete(code);
    }

}
