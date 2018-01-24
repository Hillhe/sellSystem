package com.learnspringboot.sell.service.impl;

import com.learnspringboot.sell.dataObject.OrderDetail;
import com.learnspringboot.sell.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    private final String OPENID = "013";

    @Test
    public void createOrder() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("张三");
        orderDTO.setBuyerAddress("西安");
        orderDTO.setBuyerOpenid(OPENID);
        orderDTO.setBuyerPhone("123456789");

        //购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();

        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setProductId("4445");
        orderDetail1.setProductQuantity(100);
        orderDetailList.add(orderDetail1);

        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setProductId("465465");
        orderDetail2.setProductQuantity(100);
        orderDetailList.add(orderDetail2);

        orderDTO.setOrderDetails(orderDetailList);

        OrderDTO orderDTO1 = orderServiceImpl.CreateOrder(orderDTO);
        log.info("【创建订单】 result = {}", orderDTO1);


    }

    @Test
    public void findOrderOne() {
        OrderDTO orderDTO = orderServiceImpl.findOrderOne("1516285253990892034");
        log.info("【ad】result = {}", orderDTO);
    }

    @Test
    public void findOrderList() {
        PageRequest pageRequest = new PageRequest(0, 2);
        Page<OrderDTO> orderDTOPage = orderServiceImpl.findOrderList(OPENID, pageRequest);
        Assert.assertNotEquals(0, orderDTOPage.getTotalElements());
    }

    @Test
    public void cancelOrder() {
        OrderDTO orderDTO = orderServiceImpl.findOrderOne("1516285253990892034");
        OrderDTO orderDTORes = orderServiceImpl.cancelOrder(orderDTO);
    }

    @Test
    public void finishOrder() {
        OrderDTO orderDTO = orderServiceImpl.findOrderOne("1516285253990892034");
        OrderDTO orderDTORes = orderServiceImpl.finishOrder(orderDTO);
        log.info("result = {}", orderDTORes);
    }

    @Test
    public void payOrder() {
        OrderDTO orderDTO = orderServiceImpl.findOrderOne("1516285253990892034");
        OrderDTO orderDTORes = orderServiceImpl.payOrder(orderDTO);
        log.info("result = {}", orderDTORes);
    }
}