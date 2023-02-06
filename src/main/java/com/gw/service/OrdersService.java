package com.gw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gw.common.Status;
import com.gw.dto.OrdersDto;
import com.gw.pojo.Orders;

public interface OrdersService extends IService<Orders> {
    Status<String> saveAll(OrdersDto orders);
}
