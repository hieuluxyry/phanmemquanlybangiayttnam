package com.Services;

import com.Models.Color;

import java.util.List;

public interface ColorService {

    public List<Color> getList();

    public boolean insert(Color color);

    public boolean update(Color color, String code);

    public boolean delete(String code);

}
