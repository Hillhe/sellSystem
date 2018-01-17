package com.learnspringboot.sell.enums;

import lombok.Getter;

@Getter
public enum  RespCodeEnum {

    PRODUCT_NOT_EXIST(10, "商品不存在")
    ;


    private Integer code;

    private String msg;

    RespCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
