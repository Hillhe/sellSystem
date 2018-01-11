package com.learnspringboot.sell.service;

import com.learnspringboot.sell.dataObject.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    public ProductCategory findOne(Integer categoryId);

    public List<ProductCategory> findAll();

    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    public ProductCategory save(ProductCategory productCategory);
}
