package com.learnspringboot.sell.enums;

import lombok.Getter;

@Getter
public enum  RespCodeEnum {

    PRODUCT_NOT_EXIST(10000, "商品不存在"),
    PRODUCT_STOCK_NOT_ENOUGH(10001, "库存不足"),
    ORDER_NOT_EXIST(10002, "订单不存在"),
    ORDER_DETAIL_NOT_EXIST(10003, "订单详情不存在"),
    ORDER_STATUS_UPDATE_ERROR(10004, "订单状态不正确， 不能修改"),
    ORDER_STATUS_UPDATE_FAIL(10005, "订单取消失败"),
    PARAM_ERROR(10006, "参数错误"),
    ORDER_OPENID_NOT_EQUAL(10007, "openid错误"),
    ;


    private Integer code;

    private String msg;

    RespCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
