package com.learnspringboot.sell.repository;

import com.learnspringboot.sell.dataObject.OrderMaster;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository repository;

    private final String OPENID = "013";

    @Test
    public void saveTest(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("123457");
        orderMaster.setBuyerOpenid("013");
        orderMaster.setBuyerName("贺文杰");
        orderMaster.setBuyerPhone("123456789");
        orderMaster.setOrderAmount(new BigDecimal(23.5));
        orderMaster.setBuyerAddress("西安");

        OrderMaster result = repository.save(orderMaster);
        System.out.println(result);
    }

    @Test
    public void findByBuyerOpenid() {
        PageRequest pageRequest = new PageRequest(0, 5);
        Page<OrderMaster> result =  repository.findByBuyerOpenid(OPENID, pageRequest);
        System.out.println(result.getTotalElements());
    }
}