package com.learnspringboot.sell.repository;

import com.learnspringboot.sell.dataObject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {

    //按buyeropenid分页查询
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
