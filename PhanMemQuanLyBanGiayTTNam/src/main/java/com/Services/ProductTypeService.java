package com.Services;

import com.Models.ProductType;

import java.util.List;

public interface ProductTypeService {

    public List<ProductType> getList();

    public boolean insert(ProductType productType);

    public boolean update(ProductType productType, String code);

    public boolean delete(String code);
}
