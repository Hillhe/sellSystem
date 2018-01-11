package com.learnspringboot.sell.repository;

import com.learnspringboot.sell.dataObject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer>{

    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
