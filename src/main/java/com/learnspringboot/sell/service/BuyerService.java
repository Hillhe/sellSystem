package com.learnspringboot.sell.service;

import com.learnspringboot.sell.dto.OrderDTO;

public interface BuyerService {
    public OrderDTO findOrderOne(String orderId, String openid);

    public OrderDTO cancelOrder(String orderId, String openid);
}
