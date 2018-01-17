package com.learnspringboot.sell.exception;

import com.learnspringboot.sell.enums.RespCodeEnum;

public class SellException extends RuntimeException {

    private Integer code;

    public SellException(RespCodeEnum respCodeEnum) {

        super(respCodeEnum.getMsg());
        this.code = respCodeEnum.getCode();
    }
}
