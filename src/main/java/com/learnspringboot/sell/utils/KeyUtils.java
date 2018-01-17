package com.learnspringboot.sell.utils;

import java.util.Random;

public class KeyUtils {
    /**
     * 生成主键
     */
    public static synchronized String getUniqueKey(){
        Random random = new Random();
        Integer rand = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(rand);
    }
}
