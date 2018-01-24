package com.learnspringboot.sell.service.impl;

import com.learnspringboot.sell.dataObject.ProductInfo;
import com.learnspringboot.sell.dto.CarDTO;
import com.learnspringboot.sell.enums.ProductStatusEnum;
import com.learnspringboot.sell.enums.RespCodeEnum;
import com.learnspringboot.sell.exception.SellException;
import com.learnspringboot.sell.repository.ProductInfoRepository;
import com.learnspringboot.sell.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
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

    @Override
    @Transactional
    public void increaseStock(List<CarDTO> carDTOS) {
        for (CarDTO carDTO: carDTOS){
            ProductInfo productInfo = repository.findOne(carDTO.getProductId());
            if (productInfo == null){
                throw new SellException(RespCodeEnum.PRODUCT_NOT_EXIST);
            }
            Integer stock = productInfo.getProductStock() + carDTO.getProductQuantity();
            productInfo.setProductStock(stock);
            repository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CarDTO> carDTOS) {
        for (CarDTO carDTO: carDTOS){
          ProductInfo productInfo = repository.findOne(carDTO.getProductId());
          if (productInfo == null){
              throw new SellException(RespCodeEnum.PRODUCT_NOT_EXIST);
          }

          Integer stock = productInfo.getProductStock() - carDTO.getProductQuantity();
          log.info("库存 stock = {}", productInfo.getProductStock());
          log.info("购买量 buycount = {}", carDTO.getProductQuantity());
          if (stock < 0){
              throw new SellException(RespCodeEnum.PRODUCT_STOCK_NOT_ENOUGH);
          }

          productInfo.setProductStock(stock);
          repository.save(productInfo);
        }
    }
}
