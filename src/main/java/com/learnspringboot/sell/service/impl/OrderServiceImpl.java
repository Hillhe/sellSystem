package com.learnspringboot.sell.service.impl;

import com.learnspringboot.sell.dataObject.OrderDetail;
import com.learnspringboot.sell.dataObject.OrderMaster;
import com.learnspringboot.sell.dataObject.ProductInfo;
import com.learnspringboot.sell.dto.OrderDTO;
import com.learnspringboot.sell.enums.RespCodeEnum;
import com.learnspringboot.sell.exception.SellException;
import com.learnspringboot.sell.repository.OrderDetailRepository;
import com.learnspringboot.sell.repository.OrderMasterRepository;
import com.learnspringboot.sell.service.OrderService;
import com.learnspringboot.sell.utils.KeyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl  implements OrderService{

    @Autowired
    private ProductInfoServiceImpl productInfoServiceImpl;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    public OrderDTO CreateOrder(OrderDTO orderDTO) {
        BigDecimal orderAmount = new BigDecimal(0);
        String orderId = KeyUtils.getUniqueKey();
        //1.查询商品单价，数量
        for (OrderDetail orderDetail: orderDTO.getOrderDetails()){
            ProductInfo productInfo = productInfoServiceImpl.findOne(orderDetail.getProductId());
            if (productInfo == null){
                throw new SellException(RespCodeEnum.PRODUCT_NOT_EXIST);
            }
            //2.计算订单总价
            orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);

            //3.写入到两个表中
            orderDetail.setOrderId(orderId);
            orderDetail.setDetailId(KeyUtils.getUniqueKey());
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailRepository.save(orderDetail);
        }

        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMasterRepository.save(orderMaster);
        //4.扣库存
        return null;
    }

    @Override
    public OrderDTO findOrderOne(String orderId) {
        return null;
    }

    @Override
    public Page<OrderDTO> findOrderList(String buyerOpenId, Pageable pageable) {
        return null;
    }

    @Override
    public OrderDTO cancelOrder(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO finishOrder(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO payOrder(OrderDTO orderDTO) {
        return null;
    }
}
