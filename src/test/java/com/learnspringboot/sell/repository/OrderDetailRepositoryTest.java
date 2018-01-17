package com.learnspringboot.sell.repository;

import com.learnspringboot.sell.dataObject.OrderDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository repository;

    @Test
    public void saveTest(){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("212");
        orderDetail.setProductIcon("http://xxxx.png");
        orderDetail.setProductName("haha");
        orderDetail.setProductQuantity(1150);
        orderDetail.setProductId("1226");
        orderDetail.setProductPrice(new BigDecimal(5.26));
        orderDetail.setOrderId("56565");

        OrderDetail orderDetail1 = repository.save(orderDetail);
        System.out.println(orderDetail);
    }

    @Test
    public void findByOrderId() {
        List<OrderDetail> orderDetails = repository.findByOrderId("56565");
        System.out.println(orderDetails.toString());
    }
}