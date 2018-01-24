package com.learnspringboot.sell.service.impl;

import com.learnspringboot.sell.converter.OrderMaster2OrderDTOConverter;
import com.learnspringboot.sell.dataObject.OrderDetail;
import com.learnspringboot.sell.dataObject.OrderMaster;
import com.learnspringboot.sell.dataObject.ProductInfo;
import com.learnspringboot.sell.dto.CarDTO;
import com.learnspringboot.sell.dto.OrderDTO;
import com.learnspringboot.sell.enums.OrderStatusEnum;
import com.learnspringboot.sell.enums.PayStatusEnum;
import com.learnspringboot.sell.enums.RespCodeEnum;
import com.learnspringboot.sell.exception.SellException;
import com.learnspringboot.sell.repository.OrderDetailRepository;
import com.learnspringboot.sell.repository.OrderMasterRepository;
import com.learnspringboot.sell.service.OrderService;
import com.learnspringboot.sell.utils.KeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl  implements OrderService{

    @Autowired
    private ProductInfoServiceImpl productInfoServiceImpl;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    @Transactional
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
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        //4.扣库存
        List<CarDTO> carDTOS = orderDTO.getOrderDetails().stream().map(e -> new CarDTO(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
        productInfoServiceImpl.decreaseStock(carDTOS);
        return orderDTO;
    }

    @Override
    public OrderDTO findOrderOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if (orderMaster == null){
            throw  new SellException(RespCodeEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(RespCodeEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetails(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findOrderList(String buyerOpenId, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenId, pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.contert(orderMasterPage.getContent());
        Page<OrderDTO> orderDTOPage = new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancelOrder(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        //1.判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【取消订单】 订单状态不正确， OrderId = {}, OrderStatus = {}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(RespCodeEnum.ORDER_STATUS_UPDATE_ERROR);
        }
        //2.修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster orderMasterRes = orderMasterRepository.save(orderMaster);
        if (orderMasterRes == null){
            log.error("【取消订单】 更新失败 OrderMaster = {}", orderMasterRes);
            throw new SellException(RespCodeEnum.ORDER_STATUS_UPDATE_FAIL);
        }
        //3.加库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetails())){
            log.error("【取消失败】订单中无商品 OrderMaster = {}", orderMasterRes);
            throw new SellException(RespCodeEnum.ORDER_STATUS_UPDATE_FAIL);
        }
        List<CarDTO> carDTOList = orderDTO.getOrderDetails().stream().map(e -> new CarDTO(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
        productInfoServiceImpl.increaseStock(carDTOList);

        //4.判断支付，是否退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            //TODO
        }
        return null;
    }

    @Override
    @Transactional
    public OrderDTO finishOrder(OrderDTO orderDTO) {
        OrderMaster orderMaster = orderMasterRepository.findOne(orderDTO.getOrderId());
        if (orderMaster == null){
            log.error("【订单状态】 修改失败 OrderMaster = {}", orderMaster);
            throw new SellException(RespCodeEnum.ORDER_NOT_EXIST);
        }
        if(!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单状态】 订单状态不正确， OrderId = {}, OrderStatus = {}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(RespCodeEnum.ORDER_STATUS_UPDATE_ERROR);
        }
        orderMaster.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        OrderMaster orderMasterRes = orderMasterRepository.save(orderMaster);
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO payOrder(OrderDTO orderDTO) {
        OrderMaster orderMaster = orderMasterRepository.findOne(orderDTO.getOrderId());
        if (orderMaster == null){
            log.error("【订单支付】 支付失败 OrderMaster = {}", orderMaster);
            throw new SellException(RespCodeEnum.ORDER_NOT_EXIST);
        }
        if(!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单支付】 订单状态不正确， OrderId = {}, OrderStatus = {}", orderMaster.getOrderId(), orderMaster.getOrderStatus());
            throw new SellException(RespCodeEnum.ORDER_STATUS_UPDATE_ERROR);
        }
        if(!orderMaster.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("【订单支付】 支付状态不正确， OrderId = {}, payStatus = {}", orderMaster.getOrderId(), orderMaster.getPayStatus());
            throw new SellException(RespCodeEnum.ORDER_STATUS_UPDATE_ERROR);
        }
        orderMaster.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMasterRes = orderMasterRepository.save(orderMaster);
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }
}
