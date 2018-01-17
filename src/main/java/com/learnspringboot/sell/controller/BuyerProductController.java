package com.learnspringboot.sell.controller;

import com.learnspringboot.sell.ViewObject.ProductInfoVO;
import com.learnspringboot.sell.ViewObject.ProductVO;
import com.learnspringboot.sell.ViewObject.ResultVO;
import com.learnspringboot.sell.dataObject.ProductCategory;
import com.learnspringboot.sell.dataObject.ProductInfo;
import com.learnspringboot.sell.service.impl.ProductCategoryServiceImpl;
import com.learnspringboot.sell.service.impl.ProductInfoServiceImpl;
import com.learnspringboot.sell.utils.ResultVoUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductInfoServiceImpl productInfoServiceImpl;

    @Autowired
    private ProductCategoryServiceImpl productCategoryServiceImpl;

    @GetMapping(value = "/list")
    public ResultVO productList(){
        //1.查询所有上架商品
        List<ProductInfo> productInfoList = productInfoServiceImpl.findUpAll();
        System.out.println(productInfoList.toString());
        //2.查询类目（一次性查询）//java 8 lambda表达式
        List<Integer> categoryTypeList = productInfoList.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());
        List<ProductCategory> productCategoryList = productCategoryServiceImpl.findByCategoryTypeIn(categoryTypeList);

        //3.数据拼装
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList){
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList){
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVoUtils.success(productVOList);
    }
}
