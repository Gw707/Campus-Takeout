package com.gw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gw.common.BaseContext;
import com.gw.common.Status;
import com.gw.dto.OrdersDto;
import com.gw.mapper.OrdersMapper;
import com.gw.pojo.AddressBook;
import com.gw.pojo.OrderDetail;
import com.gw.pojo.Orders;
import com.gw.pojo.User;
import com.gw.service.AddressBookService;
import com.gw.service.OrderDetailService;
import com.gw.service.OrdersService;
import com.gw.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/30 15:46
 * @Version 1.0
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Status<String> saveAll(OrdersDto orders) {
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        Long orderId = System.currentTimeMillis();
        System.out.println(orders.toString());
        System.out.println(orders.getAddressBookId());
        for (OrderDetail orderDetail : orders.getOrderDetails()) {
            orderDetail.setOrderId(orderId);
            orderDetailService.save(orderDetail);
        }
        Orders order = new Orders();
        BeanUtils.copyProperties(orders, order);
        order.setAddress(addressBook.getDetail());
        order.setNumber(orderId.toString());  //orderId -- number
        order.setPhone(addressBook.getPhone());
        order.setOrderTime(LocalDateTime.now());
        order.setUserId(BaseContext.getCurrentId());
        User user = userService.getById(BaseContext.getCurrentId());
        order.setUserName(user.getName());
        order.setCheckoutTime(LocalDateTime.now());
        order.setAmount(BigDecimal.ONE);
        System.out.println(order.toString());

        ordersService.save(order);
        return Status.success("提交成功");

    }
}
