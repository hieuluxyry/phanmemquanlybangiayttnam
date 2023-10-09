package com.Services.Implements;

import com.Models.Color;
import com.Repositories.ColorRepository;
import com.Services.ColorService;

import java.util.List;

public class ColorServiceImplement implements ColorService {

    private ColorRepository colorRepository = new ColorRepository();

    @Override
    public List<Color> getList() {
        return colorRepository.getList();
    }

    @Override
    public boolean insert(Color color) {
        return colorRepository.insert(color);
    }

    @Override
    public boolean update(Color color, String code) {
        return colorRepository.update(color, code);
    }

    @Override
    public boolean delete(String code) {
        return colorRepository.delete(code);
    }
}
