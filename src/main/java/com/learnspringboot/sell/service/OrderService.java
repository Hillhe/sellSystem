package com.learnspringboot.sell.service;

import com.learnspringboot.sell.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    /*创建对象*/
    public OrderDTO CreateOrder(OrderDTO orderDTO);

    /*查询单次订单*/
    public OrderDTO findOrderOne(String orderId);

    /*查询订单列表*/
    public Page<OrderDTO> findOrderList(String buyerOpenId, Pageable pageable);

    /*取消的名单*/
    public OrderDTO cancelOrder(OrderDTO orderDTO);

    /*完成订单*/
    public OrderDTO finishOrder(OrderDTO orderDTO);

    /*支付订单*/
    public OrderDTO payOrder(OrderDTO orderDTO);
}
