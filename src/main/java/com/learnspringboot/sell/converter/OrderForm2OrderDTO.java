package com.learnspringboot.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learnspringboot.sell.dataObject.OrderDetail;
import com.learnspringboot.sell.dto.OrderDTO;
import com.learnspringboot.sell.enums.RespCodeEnum;
import com.learnspringboot.sell.exception.SellException;
import com.learnspringboot.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderForm2OrderDTO {

    public static OrderDTO convert(OrderForm orderForm){
        OrderDTO orderDTO = new OrderDTO();
        Gson gson = new Gson();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
           orderDetailList  = gson.fromJson(orderForm.getItem(), new TypeToken<List<OrderDetail>>(){}.getType());
        } catch (Exception e){
            log.error("【对象转换错误】 string = {}", orderForm.getItem());
            throw new SellException(RespCodeEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetails(orderDetailList);
        return orderDTO;
    }
}
