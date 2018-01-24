package com.learnspringboot.sell.controller;

import com.learnspringboot.sell.ViewObject.ResultVO;
import com.learnspringboot.sell.converter.OrderForm2OrderDTO;
import com.learnspringboot.sell.dto.OrderDTO;
import com.learnspringboot.sell.enums.RespCodeEnum;
import com.learnspringboot.sell.exception.SellException;
import com.learnspringboot.sell.form.OrderForm;
import com.learnspringboot.sell.service.BuyerService;
import com.learnspringboot.sell.service.OrderService;
import com.learnspringboot.sell.utils.ResultVoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    //1.创建订单
    @PostMapping(value = "/create")
    public ResultVO<Map<String, String>> creatOrder(@Valid OrderForm orderForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【创建订单】 参数不正确， orderForm = {}", orderForm);
            throw new SellException(RespCodeEnum.PARAM_ERROR);
        }
        OrderDTO orderDTO = OrderForm2OrderDTO.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetails())){
            log.error("【创建订单】 购物车不能为空 orderForm = {}", orderForm);
            throw new SellException(RespCodeEnum.PARAM_ERROR);
        }
        OrderDTO orderDTORes = orderService.CreateOrder(orderDTO);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", orderDTORes.getOrderId());
        return ResultVoUtils.success(map);
    }

    //2.订单列表
    @GetMapping(value = "/list")
    public ResultVO<List<OrderDTO>> getOrderList(@RequestParam("openid") String openid,
                                                 @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size){
        if (StringUtils.isEmpty(openid)){
            log.error("【获取订单列表】 openid不能为空");
            throw new SellException(RespCodeEnum.PARAM_ERROR);
        }
        PageRequest pageRequest = new PageRequest(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findOrderList(openid, pageRequest);
        return ResultVoUtils.success(orderDTOPage.getContent());
    }

    //3.订单详情
    @GetMapping(value = "/detail")
    public ResultVO<OrderDTO> getOrderDetial(@RequestParam("openid") String openid, @RequestParam("orderId") String orderId ){
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(orderId)){
            log.error("【获取订单详情】 openid或orderId不能为空");
            throw new SellException(RespCodeEnum.PARAM_ERROR);
        }
        OrderDTO orderDTO = buyerService.findOrderOne(orderId, openid);
        return ResultVoUtils.success(orderDTO);
    }


    //4.取消订单
    @PostMapping(value = "/cancel")
    public ResultVO<OrderDTO> cancelOrder(@RequestParam("openid") String openid, @RequestParam("orderId") String orderId ){
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(orderId)){
            log.error("【取消订单】 openid或orderId不能为空");
            throw new SellException(RespCodeEnum.PARAM_ERROR);
        }
        OrderDTO orderDTO = buyerService.cancelOrder(orderId, openid);
        return ResultVoUtils.success();
    }
}
