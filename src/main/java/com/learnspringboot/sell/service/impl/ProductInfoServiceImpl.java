package com.learnspringboot.sell.service.impl;

import com.learnspringboot.sell.dataObject.ProductInfo;
import com.learnspringboot.sell.enums.ProductStatusEnum;
import com.learnspringboot.sell.repository.ProductInfoRepository;
import com.learnspringboot.sell.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductInfoRepository repository;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo Save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }
}
