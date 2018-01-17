package com.learnspringboot.sell.enums;

import lombok.Getter;

@Getter
public enum ProductStatusEnum {
    UP(0, "商品上架"),
    DOWN(1, "商品下架")
    ;
    private Integer code;
    private String msg;

    ProductStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
