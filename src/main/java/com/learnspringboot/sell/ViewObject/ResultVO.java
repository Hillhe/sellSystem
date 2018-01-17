package com.learnspringboot.sell.ViewObject;

import lombok.Data;

/**
 * http请求的最外层对象
 */

@Data
public class ResultVO<T> {

    private Integer code;

    private String msg;

    private T data;
}
