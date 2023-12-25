package com.newmaster.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newmaster.reggie.pojo.Orders;

public interface OrdersService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    void submit(Orders orders);
}
