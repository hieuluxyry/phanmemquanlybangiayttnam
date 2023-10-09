package com.Services;

import com.Models.Size;

import java.util.List;

public interface SizeService {

    public List<Size> getList();

    public boolean insert(Size size);

    public boolean update(Size size, String code);

    public boolean delete(String code);
}
