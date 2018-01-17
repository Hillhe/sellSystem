package com.learnspringboot.sell.repository;

import com.learnspringboot.sell.dataObject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {
    public List<ProductInfo> findByProductStatus(Integer productStatus);
}
