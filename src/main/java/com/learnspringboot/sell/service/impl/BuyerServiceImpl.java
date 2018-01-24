package com.learnspringboot.sell.service.impl;

import com.learnspringboot.sell.dto.OrderDTO;
import com.learnspringboot.sell.enums.RespCodeEnum;
import com.learnspringboot.sell.exception.SellException;
import com.learnspringboot.sell.service.BuyerService;
import com.learnspringboot.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOrderOne(String orderId, String openid) {
        return checkOrderDTOOnwer(orderId, openid);
    }

    @Override
    public OrderDTO cancelOrder(String orderId, String openid) {
        OrderDTO orderDTO = checkOrderDTOOnwer(orderId, openid);
        if (orderDTO == null){
            log.error("【取消订单】 订单不存在");
            throw new SellException(RespCodeEnum.ORDER_NOT_EXIST);
        }
        return orderService.cancelOrder(orderDTO);
    }

    private OrderDTO checkOrderDTOOnwer(String orderId, String openid){
        OrderDTO orderDTO = orderService.findOrderOne(orderId);
        if (orderDTO == null){
            return null;
        }
        if (!orderDTO.getBuyerOpenid().equalsIgnoreCase(openid)){
            log.error("【查询订单详情】 不是本人");
            throw new SellException(RespCodeEnum.ORDER_OPENID_NOT_EQUAL);
        }
        return orderDTO;
    }
}
