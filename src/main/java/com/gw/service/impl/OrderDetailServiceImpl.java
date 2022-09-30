package com.gw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gw.mapper.OrderDetailMapper;
import com.gw.pojo.OrderDetail;
import com.gw.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/30 15:52
 * @Version 1.0
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
