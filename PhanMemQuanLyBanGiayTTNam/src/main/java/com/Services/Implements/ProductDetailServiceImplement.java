package com.Services.Implements;

import com.CustomModels.ProductDetailCustomModel;
import com.Models.ProductDetails;
import com.Repositories.ProductDetailPepository;
import com.Services.ProductDetailService;

import java.util.List;

public class ProductDetailServiceImplement implements ProductDetailService {

    private ProductDetailPepository productDetailPepository = new ProductDetailPepository();

    @Override
    public List<ProductDetailCustomModel> getListProductDetal() {
        return productDetailPepository.getListProductDetail(1);
    }

    @Override
    public List<ProductDetailCustomModel> getListProductDetalHide() {
        return productDetailPepository.getListProductDetail(0);
    }

    @Override
    public List<ProductDetailCustomModel> getListByCode(String code) {
        return productDetailPepository.getListByCode(code);
    }

    @Override
    public List<ProductDetailCustomModel> getListByName(String name) {
        return productDetailPepository.getListByName(name);
    }

    @Override
    public List<ProductDetailCustomModel> getListByProductType(String data) {
        return productDetailPepository.getListByProductType(data);
    }

    @Override
    public List<ProductDetailCustomModel> getListBySize(String data) {
        return productDetailPepository.getListBySize(data);
    }

    @Override
    public List<ProductDetailCustomModel> getListByColor(String data) {
        return productDetailPepository.getListByColor(data);
    }

    @Override
    public List<ProductDetailCustomModel> getListBySubstance(String data) {
        return productDetailPepository.getListBySubstance(data);
    }

    @Override
    public boolean insert(ProductDetails productDetails) {
        return productDetailPepository.insert(productDetails);
    }

    @Override
    public boolean update(ProductDetails productDetails, String code) {
        return productDetailPepository.update(productDetails, code);
    }

    @Override
    public boolean hideOrShow(String code, int status) {
        return productDetailPepository.hideOrShow(code, status);
    }

    @Override
    public List<ProductDetails> getList() {
        return productDetailPepository.getList();
    }

    @Override
    public String getByID(String code) {
        return productDetailPepository.getByID(code);
    }

    @Override
    public boolean updateSoLuong(String code, int soLuong) {
        return productDetailPepository.updateSoLuong(code, soLuong);
    }

    @Override
    public Float getByDonGia(String code) {
        return productDetailPepository.getByDonGia(code);
    }

    @Override
    public Integer getBySoLuong(String code) {
        return productDetailPepository.getBySoLuong(code);
    }

    @Override
    public String getByNameProduct(String code) {
        return productDetailPepository.getByName(code, productDetailPepository.getByNameProduct);
    }

    @Override
    public String getByNameType(String code) {
        return productDetailPepository.getByName(code, productDetailPepository.getByNameType);
    }

    @Override
    public String getByNameSize(String code) {
        return productDetailPepository.getByName(code, productDetailPepository.getByNameSize);
    }

    @Override
    public String getByNameColor(String code) {
        return productDetailPepository.getByName(code, productDetailPepository.getByNameColor);
    }

    @Override
    public String getByNameSubtance(String code) {
        return productDetailPepository.getByName(code, productDetailPepository.getByNameSubtance);
    }
}
