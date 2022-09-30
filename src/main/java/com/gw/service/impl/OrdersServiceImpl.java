package com.gw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gw.mapper.OrdersMapper;
import com.gw.pojo.Orders;
import com.gw.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/30 15:46
 * @Version 1.0
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
}
