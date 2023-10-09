package com.Services;

import com.CustomModels.ProductDetailCustomModel;
import com.Models.ProductDetails;

import java.util.List;

public interface ProductDetailService {

    public List<ProductDetailCustomModel> getListProductDetal();

    public List<ProductDetailCustomModel> getListProductDetalHide();

    public List<ProductDetailCustomModel> getListByCode(String code);

    public List<ProductDetailCustomModel> getListByName(String name);

    public List<ProductDetailCustomModel> getListByProductType(String data);

    public List<ProductDetailCustomModel> getListBySize(String data);

    public List<ProductDetailCustomModel> getListByColor(String data);

    public List<ProductDetailCustomModel> getListBySubstance(String data);

    public boolean insert(ProductDetails productDetails);

    public boolean update(ProductDetails productDetails, String code);

    public boolean hideOrShow(String code, int status);

    public List<ProductDetails> getList();

    public String getByID(String code);

    public boolean updateSoLuong(String code, int soLuong);

    public Float getByDonGia(String code);

    public Integer getBySoLuong(String code);

    public String getByNameProduct(String code);

    public String getByNameType(String code);

    public String getByNameSize(String code);

    public String getByNameColor(String code);

    public String getByNameSubtance(String code);
}
