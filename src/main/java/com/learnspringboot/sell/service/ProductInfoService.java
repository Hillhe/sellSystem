package com.learnspringboot.sell.service;

import com.learnspringboot.sell.dataObject.ProductInfo;
import com.learnspringboot.sell.dto.CarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductInfoService {

    public ProductInfo findOne(String productId);

    public List<ProductInfo> findUpAll();

    public Page<ProductInfo> findAll(Pageable pageable);

    public ProductInfo Save(ProductInfo productInfo);

    //加库存
    public void increaseStock(List<CarDTO> carDTOS);

    //减库存
    public void decreaseStock(List<CarDTO> carDTOS);
}
